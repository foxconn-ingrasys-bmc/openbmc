SUMMARY = "U-Boot bootloader image creation tool"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
SECTION = "bootloader"

DEPENDS = "openssl"

#SRCREV = "b5452c0d29bca88f27ce90ce500565b546bf18e3"
SRCREV = "a095e797fc3f815566e776f409d8139c7fc82d10"
#UBRANCH = "foxconn-v2016.07-aspeed-openbmc"
UBRANCH = "foxconn-tpe"
#SRC_URI = "git://git@github.com/foxconn-ingrasys-bmc/u-boot.git;branch=${UBRANCH};protocol=https"
SRC_URI = "git://git@github.com/willenlee/u-boot.git;branch=${UBRANCH};protocol=https"

PV = "v2016.07+git${SRCPV}"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = 'CROSS_COMPILE="${TARGET_PREFIX}" CC="${CC} ${CFLAGS} ${LDFLAGS}" STRIP=true V=1'

do_compile () {
	oe_runmake sandbox_defconfig
	oe_runmake cross_tools NO_SDL=1
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 tools/mkimage ${D}${bindir}/uboot-mkimage
	ln -sf uboot-mkimage ${D}${bindir}/mkimage
}

BBCLASSEXTEND = "native nativesdk"
