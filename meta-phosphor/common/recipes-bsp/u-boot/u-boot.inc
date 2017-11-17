LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRCREV = "c7116f022f403fdb47b7129a11438bf6b19eb264"
UBRANCH = "foxconn-v2016.07-aspeed-openbmc-g2"
SRC_URI = "git://git@github.com/foxconn-bmc-ks/u-boot.git;branch=${UBRANCH};protocol=https"

SRC_URI += "file://0001-configs-ast-Add-redundnant-env.patch"

SRC_URI += "${@bb.utils.contains('MACHINE_FEATURES', 'obmc-ubi-fs', \
    'file://0001-config-ast-common-hack-bootopts.patch \
     file://0004-config-ast-common-ubi-bootops.patch', '', d)}"

PV = "v2016.07+git${SRCPV}"
