SUMMARY = "OpenBMC fan algorithm"
DESCRIPTION = "OpenBMC fan algorithm."
PR = "r1"

inherit skeleton-sdbus
inherit obmc-phosphor-dbus-service

RDEPENDS_${PN} += "libsystemd"
SKELETON_DIR = "fan_algorithm"

DBUS_SERVICE_${PN} += "org.openbmc.control.Fans-Algorithm.service"
