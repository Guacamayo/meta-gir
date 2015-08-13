LICENSE = "LGPLv2.1"

LIC_FILES_CHKSUM = "file://COPYING;md5=a916467b91076e631dd8edb7424769c7"

inherit autotools pkgconfig gnome pythonnative

DEPENDS += "python-pycairo"
PACKAGES += "${PN}-lib"

SRCNAME="pygobject"
SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/${SRCNAME}/${@gnome_verdir("${PV}")}/${SRCNAME}-${PV}.tar.xz \
	   file://fix-configure.patch \
	   file://fix-tests.patch \
	  "
SRC_URI[md5sum] = "0cbcda00d9276f78040d361d1611a6a0"
SRC_URI[sha256sum] = "f457b1d7f6b8bfa727593c3696d2b405da66b4a8d34cd7d3362ebda1221f0661"

S = "${WORKDIR}/${SRCNAME}-${PV}"

EXTRA_OECONF += "--enable-introspection"

do_configure_prepend () {
    sed -i -e "s|PYTHON=/usr/bin/python|PYTHON=${PYTHON}|" \
        ${S}/configure.ac
}

FILES_${PN} = "${libdir}/python*"
FILES_${PN}-lib = "${libdir}/lib*.so.*"
FILES_${PN}-dev += "${bindir} ${datadir}"
FILES_${PN}-dbg += "${libdir}/python*/*/gi/.debug ${libdir}/python*/*/gi/_glib/.debug ${libdir}/python*/*/gi/_gobject/.debug"
