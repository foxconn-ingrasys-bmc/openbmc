SUMMARY = "Phosphor python library"
DESCRIPTION = "Phosphor python library."
HOMEPAGE = "http://github.com/openbmc/pyphosphor"
PR = "r1"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit allarch
inherit setuptools
FOXCONN_BRANCH = "foxconn-hgx1"
SRC_URI += "git://github.com/foxconn-ingrasys-bmc/pyphosphor;branch=${FOXCONN_BRANCH}"

SRCREV = "9fa44ad683781f82b6016ede003afc07f17c9f22"

S = "${WORKDIR}/git"
