SUMMARY = "OpenBMC fan command tool"
DESCRIPTION = "OpenBMC fan command tool."
PR = "r1"

inherit skeleton-sdbus
#inherit obmc-phosphor-dbus-service

SKELETON_DIR = "fan_tool"

