#KBRANCH ?= "foxconn-sthelens-4.7"
KBRANCH ?= "foxconn-hgx1-4.7"
LINUX_VERSION ?= "4.7"
#SRCREV="ad6f155e7b922d3f278337d7618dd88b2dc6e6f3"
SRCREV="acb170ec724d099a456d126b547c891128a741f3"

require common/recipes-kernel/linux/linux-obmc.inc
