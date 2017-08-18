SUMMARY = "Settings DBUS object"
DESCRIPTION = "Settings DBUS object"
HOMEPAGE = "http://github.com/openbmc/phosphor-settingsd"
PR = "r1"

inherit obmc-phosphor-license
inherit obmc-phosphor-systemd

RDEPENDS_${PN} += "python-dbus python-pygobject"

FOXCONN_BRANCH = "foxconn-sthelens"
SRC_URI += "git://github.com/foxconn-ingrasys-bmc/phosphor-settingsd;branch=${FOXCONN_BRANCH}"

SRCREV = "b96543d35c01983f07b2d6b60e205ed1fc03109e"

S = "${WORKDIR}/git"

do_install() {
        install -d ${D}/${sbindir}
        install ${S}/settings_file.py ${D}/${sbindir}
        install ${S}/settings_manager.py ${D}/${sbindir}
}

