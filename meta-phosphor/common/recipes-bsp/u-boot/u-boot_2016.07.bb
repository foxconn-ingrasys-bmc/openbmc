require recipes-bsp/u-boot/u-boot.inc

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
DEPENDS += "dtc-native"

SRCREV = "edbf58646c8b3e72a1b98d1012846fbbf5034f59"
UBRANCH = "foxconn-hgx1-v2016.07"
SRC_URI = "git://git@github.com/foxconn-ingrasys-bmc/u-boot.git;branch=${UBRANCH};protocol=https"

PV = "v2016.07+git${SRCPV}"
