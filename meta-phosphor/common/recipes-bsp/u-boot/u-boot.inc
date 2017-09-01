LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRCREV = "f823c0114c14f635af84af7c1b283e3dfdd6f579"
UBRANCH = "foxconn-v2016.07-aspeed-openbmc-g2"
SRC_URI = "git://git@github.com/foxconn-bmc-ks/u-boot.git;branch=${UBRANCH};protocol=https"

SRC_URI += "${@bb.utils.contains('MACHINE_FEATURES', 'obmc-ubi-fs', \
    'file://0001-config-ast-common-hack-bootopts.patch \
     file://0002-Add-MTD-support-to-Aspeed-Flash-driver.patch \
     file://0003-Add-MTD-and-UBI-support-to-ast-g5.patch \
     file://0004-config-ast-common-ubi-bootops.patch', '', d)}"

PV = "v2016.07+git${SRCPV}"

SRC_URI += "file://0001-configs-ast-Add-redundnant-env.patch"
