# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
# SRC_URI = "git://git@github.com/cu-ecen-aeld/<your assignments repo>;protocol=ssh;branch=master"

# jayp: using "gitsm:" main and also fetched submodules recursively. 
# 		Since I am not using anything from submodule, I can opt for git: only too.
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-JayPatel17-py.git;protocol=ssh;branch=master"

PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
#SRCREV = "f99b82a5d4cb2a22810104f89d4126f52f4dfaba"\

# jayp: commit id from git repo assignments-3-and-later-JayPatel17-py
#		tag - assignment-6-part-1 
SRCREV = "9362fec361e5294252b55ba58d8f5faea1bf3022"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo

# jayp: work directory is where my assignment directory
#		${WORKDIR} is our workspace directory(tmp/work/...)
#		receipe fetched from git will be under ${WORKDIR}/git
#		since our source code of aesdsocket is under server, S will be like below
S = "${WORKDIR}/git/server"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone

# jayp: These are files/directory I want to include in my final packge called ${PN}. 
#		files added to rootfs(root file system).
#		do_install blindly follow steps we mentioned. It's not smart to check, perform, remember.
FILES:${PN} += "${bindir}/aesdsocket"

# TODO: customize these as necessary for any libraries you need for your application
# (and remove comment)

#jayp: adding -pthread library as we aesdsocket application is multithreaded.
TARGET_LDFLAGS += "-pthread"

# jayp: for start up script of yocto default SysV(system V)
#		inherit pulls functionality from bbclass file. (meta/classes/update-rc.d.bbclass)
#		
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "myservice"
INITSCRIPT_PARAMS:${PN} = "defaults"

do_configure () {
	:
}

do_compile () {
	oe_runmake
}

do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb

	# jayp: installing bin directory and puting aesdsocket application in it.
	#		${bindir} is /usr/bin directory
	#		${sysconfdir} is /etc directory
	#		${D} is the Yocto staging directory (something like tmp/work/.../image/).
	install -d ${D}${bindir}
	install -m 0755 ${S}/aesdsocket ${D}${bindir}/aesdsocket

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 myservice ${D}${sysconfdir}/init.d/myservice
}
