#KBRANCH ?= "foxconn-sthelens-4.7"
KBRANCH ?= "foxconn-hgx1-4.7"
LINUX_VERSION ?= "4.7"
#SRCREV="ad6f155e7b922d3f278337d7618dd88b2dc6e6f3"
SRCREV="6faa31f9efbb8e36c6c7819f9f9c674c2d2f7e2d"

require common/recipes-kernel/linux/linux-obmc.inc
