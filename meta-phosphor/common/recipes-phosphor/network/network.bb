SUMMARY = "Network DBUS object"
DESCRIPTION = "Network DBUS object"
HOMEPAGE = "http://github.com/openbmc/phosphor-networkd"
PR = "r1"

inherit obmc-phosphor-license
inherit obmc-phosphor-systemd
inherit obmc-phosphor-sdbus-service

SYSTEMD_SERVICE_${PN} += "network.service network-update-dns.service"

RDEPENDS_${PN} += "python-dbus python-pygobject"

FOXCONN_BRANCH = "foxconn-hgx1"
SRC_URI += "git://github.com/foxconn-ingrasys-bmc/phosphor-networkd;branch=${FOXCONN_BRANCH}"

SRCREV = "2b9234809e3e5586008913287f5bdb46de14a7fb"

S = "${WORKDIR}/git"

do_install() {
        install -d ${D}/${sbindir}
        install ${S}/netman.py ${D}/${sbindir}
        install ${S}/netman_watch_dns ${D}/${sbindir}
}
