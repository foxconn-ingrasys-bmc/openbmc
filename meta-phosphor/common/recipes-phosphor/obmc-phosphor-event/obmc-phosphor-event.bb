SUMMARY = "Phosphor OpenBMC Event Management"
DESCRIPTION = "Phosphor OpenBMC event management reference implementation."
HOMEPAGE = "https://github.com/openbmc/phosphor-event"
PR = "r1"


inherit obmc-phosphor-license
inherit obmc-phosphor-event-mgmt
inherit obmc-phosphor-sdbus-service
inherit obmc-phosphor-c-daemon

TARGET_CPPFLAGS += "-std=c++11 -fpic"

FOXCONN_BRANCH = "foxconn-hgx1-mast"
SRC_URI += "git://github.com/foxconn-ingrasys-bmc/phosphor-event;branch=${FOXCONN_BRANCH}"

SRCREV = "4d85cbdb2b4296db649f5c510d116d6fbc37ecab"

RDEPENDS_${PN} += "libsystemd"
DEPENDS += "systemd"


S = "${WORKDIR}/git"
INSTALL_NAME = "event_messaged"

do_install() {
        install -d ${D}/var/lib/obmc/events/
        install -m 0755 -d ${D}${sbindir}
        install -m 0755 ${S}/${INSTALL_NAME} ${D}/${sbindir}/obmc-phosphor-eventd
}
