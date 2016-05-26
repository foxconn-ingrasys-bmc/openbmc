DESCRIPTION = "Linux kernel for OpenBMC"
SECTION = "kernel"
LICENSE = "GPLv2"

KBRANCH ?= "dev-4.4"
KCONFIG_MODE="--alldefconfig"

SRC_URI = "git://github.com/SteveLinCH/linux;protocol=git;branch=${KBRANCH}"

LINUX_VERSION ?= "4.4"
LINUX_VERSION_EXTENSION ?= "-${SRCREV}"

SRCREV="70fece23da977d798ac274f07b07d2c35f4e8909"

PV = "${LINUX_VERSION}+git${SRCPV}"

COMPATIBLE_MACHINE_${MACHINE} = "openbmc"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc
