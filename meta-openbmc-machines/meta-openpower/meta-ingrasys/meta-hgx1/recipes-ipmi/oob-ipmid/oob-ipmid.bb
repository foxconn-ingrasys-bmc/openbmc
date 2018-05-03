DESCRIPTION = "IPMI over LAN (RMCP/IPMIv1.5) Server"

inherit obmc-phosphor-license
inherit obmc-phosphor-systemd

BB_NO_NETWORK = "0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/foxconn-ingrasys-bmc/oob-ipmid.git;protocol=https;"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

SYSTEMD_SERVICE_${PN} += "${PN}.service"

do_configure[noexec] = "1"

do_compile() {
        oe_runmake DESTDIR=${D} LIBDIR=${libdir} INCDIR=${includedir}
}

do_install() {
        oe_runmake install DESTDIR=${D} BINDIR=${bindir}
}

