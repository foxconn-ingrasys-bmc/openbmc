require recipes-bsp/u-boot/u-boot.inc

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
DEPENDS += "dtc-native"

SRCREV = "b5452c0d29bca88f27ce90ce500565b546bf18e3"
UBRANCH = "foxconn-v2016.07-aspeed-openbmc"
SRC_URI = "git://git@github.com/foxconn-ingrasys-bmc/u-boot.git;branch=${UBRANCH};protocol=https"

PV = "v2016.07+git${SRCPV}"
