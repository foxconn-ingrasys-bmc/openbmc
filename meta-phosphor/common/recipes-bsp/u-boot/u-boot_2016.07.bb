require recipes-bsp/u-boot/u-boot.inc

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
DEPENDS += "dtc-native"

SRCREV = "c633860f1e898ecbe71cf87509b9b2dbdad0fb93"
UBRANCH = "foxconn-hgx1-v2016.07"
SRC_URI = "git://git@github.com/foxconn-ingrasys-bmc/u-boot.git;branch=${UBRANCH};protocol=https"

PV = "v2016.07+git${SRCPV}"
