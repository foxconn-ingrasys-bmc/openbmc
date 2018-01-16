#**************************************************************
#*                                                            *
#*   Copyright (C) Microsoft Corporation. All rights reserved.*
#*                                                            *
#**************************************************************

SUMMARY = "OpenBMC Hardware Watchdog"
DESCRIPTION = "OpenBMC Hardware Watchdog Daemon"

inherit obmc-phosphor-license
inherit obmc-phosphor-systemd

SRC_URI += "file://obmc-ast-watchdog.service"
SRC_URI += "file://obmc-ast-watchdog.c"

SYSTEMD_SERVICE_${PN} += "obmc-ast-watchdog.service"

S = "${WORKDIR}"

do_compile() {
	${CC} obmc-ast-watchdog.c -o obmc-ast-watchdog
}

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 obmc-ast-watchdog ${D}${sbindir}
}
