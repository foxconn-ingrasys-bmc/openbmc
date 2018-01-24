DESCRIPTION = "IPMI over LAN (RMCP/IPMIv1.5) Server"

HOMEPAGE = "https://github.com/foxconn-bmc-ks/fru-util"

DEPENDS += "zlog"
inherit obmc-phosphor-license
INSANE_SKIP_fru-util += "dev-deps"
INSANE_SKIP_fru-util-dev += "dev-elf"

BB_NO_NETWORK = "0"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
#RDEPENDS_${PN} = "bash python"


FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

FOXCONN_BRANCH="foxconn-g2"
SRC_URI = "git://github.com/foxconn-bmc-ks/fru-util;branch=${FOXCONN_BRANCH}"
SRCREV = "9c171002298689d29fcadd1b48794f25aa8ddacb"
S = "${WORKDIR}/git"

do_compile() {
        oe_runmake
}

do_install() {
        install -Dm755 ${B}/fru-util/bin/ocs-fru ${D}/${sbindir}/ocs-fru
        install -Dm755 ${B}/frui2clib/lib/libocsfrui2c.so ${D}/${libdir}/libocsfrui2c.so
        install -Dm755 ${B}/Ocslog/lib/libocslog.so ${D}/${libdir}/libocslog.so
        install -Dm755 ${B}/SemLock/lib/libocslock.so ${D}/${libdir}/libocslock.so
}
