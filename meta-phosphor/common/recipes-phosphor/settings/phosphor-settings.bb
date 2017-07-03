SUMMARY = "Settings DBUS object"
DESCRIPTION = "Settings DBUS object"
HOMEPAGE = "http://github.com/openbmc/phosphor-settingsd"
PR = "r1"
FOXCONN_BRANCH = "foxconn-g2"

inherit allarch
inherit obmc-phosphor-license
inherit setuptools
inherit obmc-phosphor-dbus-service

DBUS_SERVICE_${PN} = "org.openbmc.settings.Host.service"

DEPENDS += "python-pyyaml-native"
RDEPENDS_${PN} += "python-dbus python-pygobject"
PROVIDES += "virtual/obmc-settings-mgmt"
RPROVIDES_${PN} += "virtual-obmc-settings-mgmt"

SRC_URI += "git://github.com/foxconn-bmc-ks/phosphor-settingsd;protocol=git;branch=${FOXCONN_BRANCH}"
SRCREV = "7b2551ace0ab3a14dcf78a60f28db74b9f3c3e55"

S = "${WORKDIR}/git"
