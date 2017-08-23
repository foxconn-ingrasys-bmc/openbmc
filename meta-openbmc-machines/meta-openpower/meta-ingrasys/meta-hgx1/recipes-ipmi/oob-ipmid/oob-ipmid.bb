DESCRIPTION = "IPMI over LAN (RMCP/IPMIv1.5) Server"

HOMEPAGE = "https://csibmc.visualstudio.com/J2010/_git/oob-ipmid"
LICENSE = "Microsoft"

inherit obmc-phosphor-license
inherit obmc-phosphor-systemd

BB_NO_NETWORK = "0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

#SRC_URI = "git://csibmc@csibmc.visualstudio.com:22/J2010/_git/oob-ipmid;protocol=ssh;"
SRC_URI = "git://github.com/danielweitw/oob-ipmid.git;protocol=https;"
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

