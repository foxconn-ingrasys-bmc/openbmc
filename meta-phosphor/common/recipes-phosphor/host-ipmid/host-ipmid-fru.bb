SUMMARY = "Phosphor OpenBMC WriteFRU for OpenPOWER systems"
DESCRIPTION = "Phosphor OpenBMC WriteFRU for  OpenPOWER based systems"
HOMEPAGE = "https://github.com/openbmc/ipmi-fru-parser"
PR = "r1"

inherit obmc-phosphor-license

DEPENDS += " \
        systemd \
        host-ipmid \
        "

RDEPENDS_${PN} += "libsystemd"

TARGET_CFLAGS += " -fpic -std=gnu++14"

SRC_URI += "git://github.com/foxconn-ingrasys-bmc/ipmi-fru-parser"

SRCREV = "a26ed7375261ebcfffa68cf478bcb8f2204c6c92"
#SRCREV = "155c34fbb61071f5b51240c4a50b49391e0877c1"

FILES_SOLIBSDEV += "${libdir}/host-ipmid/lib*${SOLIBSDEV}"
FILES_${PN} += "${libdir}/host-ipmid/lib*${SOLIBS}"
FILES_${PN}-dbg += "${libdir}/host-ipmid/.debug/lib*${SOLIBS}"

S = "${WORKDIR}/git"

do_install() {
        oe_runmake install DESTDIR=${D} LIBDIR=${libdir} BINDIR=${sbindir}
}
