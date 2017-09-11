# In general this class should only be used by board layers
# that keep their machine-readable-workbook in the skeleton repository.

inherit allarch
inherit setuptools
inherit pythonnative

DEPENDS += "python"
#FOXCONN_BRANCH = "foxconn-openbmc-v1.0-stable"
FOXCONN_BRANCH = "tpe-willen"
#SRC_URI += "git://github.com/foxconn-ingrasys-bmc/skeleton;subpath=configs;branch=${FOXCONN_BRANCH}"
SRC_URI += "git://github.com/willenlee/skeleton;subpath=configs;branch=${FOXCONN_BRANCH}"
S = "${WORKDIR}/configs"

python() {
	machine = d.getVar('MACHINE', True).capitalize() + '.py'
	d.setVar('_config_in_skeleton', machine)
}

do_make_setup() {
        cp ${S}/${_config_in_skeleton} \
                ${S}/obmc_system_config.py
        cat <<EOF > ${S}/setup.py
from distutils.core import setup

setup(name='${BPN}',
    version='${PR}',
    py_modules=['obmc_system_config'],
    )
EOF
}

addtask make_setup after do_patch before do_configure
