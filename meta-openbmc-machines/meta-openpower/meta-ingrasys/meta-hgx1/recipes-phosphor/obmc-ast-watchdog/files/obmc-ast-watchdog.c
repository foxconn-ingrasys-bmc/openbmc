/*************************************************************
*                                                            *
*   Copyright (C) Microsoft Corporation. All rights reserved.*
*                                                            *
*************************************************************/

#include <errno.h>
#include <fcntl.h>
#include <linux/watchdog.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <time.h>
#include <unistd.h>

#define MAX_NONRESPONSE 10

struct Service {
    /* Check service by heartbeat file.
     *
     * Hearbeat file is periodically recreated by this program. Service is
     * expected to delete this file to prove its responsiveness.
     *
     * This can be NULL. */
    const char *heartbeat;

    /* Check service by PID file.
     *
     * Service will be considered responsive as long as /proc/PID exists.
     *
     * This can be NULL. */
    const char *pidfile;

    /* How many times heartbeat test or pidfile test fails.
     *
     * Should this be larger than MAX_NONRESPONSE, this service is considered
     * dead. */
    uint32_t nNonresponse;
};

static struct Service Services[] = {
    { "/run/obmc/watch_redfish", NULL, 0 },
    { "/run/obmc/watch_hwmon", NULL, 0 },
    { NULL, "/run/obmc-phosphor-event.pid", 0 },
};

static const int SERVICE_COUNT = sizeof(Services) / sizeof(struct Service);

static void save_pid(void) {
    pid_t pid = 0;
    FILE *pidfile = NULL;
    pid = getpid();
    if (!(pidfile = fopen("/run/obmc-ast-watchdog.pid", "w"))) {
        fprintf(stderr, "ERROR on fopen(/run/obmc-ast-watchdog.pid)\n");
        return;
    }
    fprintf(pidfile, "%d\n", pid);
    fclose(pidfile);
}

static void create_heartbeat_file(const struct Service *srv) {
    FILE *fp = NULL;

    if (srv->heartbeat) {
        if (!(fp = fopen(srv->heartbeat, "w+"))) {
            fprintf(stderr, "ERROR on fopen(%s): %s\n",
                    srv->heartbeat, strerror(errno));
            return;
        }
        fclose(fp);
    }
}

static int is_responsive(const struct Service *srv) {
    int responsive = 1;
    FILE *fp = NULL;
    int pid = 0;
    char path[64] = {0};

    if (responsive && srv->heartbeat) {
        responsive = (access(srv->heartbeat, F_OK) == -1);
    }

    if (responsive && srv->pidfile) {
        if ((fp = fopen(srv->pidfile, "r"))) {
            if (fscanf(fp, "%d", &pid) != 1) {
                responsive = 0;
            }
            fclose(fp);

            if (responsive) {
                if (snprintf(path, 64, "/proc/%d", pid) < 64) {
                    responsive = !access(path, F_OK);
                } else {
                    fprintf(stderr, "ERROR not enough space for process path\n");
                    responsive = 0;
                }
            }
        } else {
            responsive = 0;
        }
    }

    return responsive;
}

static void wait_until_all_services_responsive(void) {
    int i;

    for (i = 0; i < SERVICE_COUNT; i++) {
        create_heartbeat_file(&Services[i]);
    }

    for (i = 0; i < SERVICE_COUNT; i++) {
        while (!is_responsive(&Services[i])) {
            sleep(5);
        }
    }
}

static int enable_WDT1(int *wdt1) {
    int fd = open("/dev/watchdog", O_RDWR);

    if (fd != -1) {
        *wdt1 = fd;
        return 0;
    } else {
        fprintf(stderr, "ERROR on open(/dev/watchdog): %s\n", strerror(errno));
        return -1;
    }
}

static void disable_WDT2(void) {
    //system("devmem 0x1e78502c 32 0");
    system("devmem 0x1e78502c 8 0x92");
}

static int all_services_alive(void) {
    int alive = 1;
    int i;

    for (i = 0; i < SERVICE_COUNT; i++) {
        if (is_responsive(&Services[i])) {
            Services[i].nNonresponse = 0;
            create_heartbeat_file(&Services[i]);
        } else {
            Services[i].nNonresponse++;
            if (Services[i].nNonresponse > MAX_NONRESPONSE) {
                alive = 0;
            }
        }
    }

    return alive;
}

static void kick_WDT1(int wdt1) {
    if (ioctl(wdt1, WDIOC_KEEPALIVE)) {
        fprintf(stderr, "ERROR on ioctl(wdt1, WDIOC_KEEPALIVE): %s\n",
                strerror(errno));
    }
}

static void disable_WDT1(int wdt1) {
    close(wdt1);
}

int main(int argc, char** argv) {
    int wdt1 = -1;
    struct timeval tv;

    /* Save PID to pre-defined PID file so that BMC Health sensor can count how
     * many times has this program restarted. */
    save_pid();

    /* It takes some time for all services checked by this program to be
     * responsive. We need to wait for them to be running before enabling WDT1,
     * or WDT1 may time out too soon. */
    wait_until_all_services_responsive();

    if (enable_WDT1(&wdt1)) {
        return -1;
    }

    /* Disable WDT2 to indicate successfully booting from primary flash. */
    disable_WDT2();

    /* Check services' aliveness every 5 seconds. Only when all services are
     * alive should WDT1 be kicked. */
    while (1) {
        //if (all_services_alive()) {
            kick_WDT1(wdt1);
        //}
        tv.tv_sec = 5;
        tv.tv_usec = 0;
        select(0, NULL, NULL, NULL, &tv);
    }

    // should never reach here
    disable_WDT1(wdt1);

    return 0;
}
