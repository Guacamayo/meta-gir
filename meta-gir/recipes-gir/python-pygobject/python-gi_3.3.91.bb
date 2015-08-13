LICENSE = "LGPLv2.1"

LIC_FILES_CHKSUM = "file://COPYING;md5=a916467b91076e631dd8edb7424769c7"

inherit autotools pkgconfig gnome pythonnative

DEPENDS += "python-pycairo"
RDEPENDS_${PN} += "python-pkgutil"
PACKAGES += "${PN}-lib"

SRCNAME="pygobject"
SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/${SRCNAME}/${@gnome_verdir("${PV}")}/${SRCNAME}-${PV}.tar.xz \
	   file://fix-configure.patch \
	   file://fix-tests.patch \
	  "
SRC_URI[md5sum] = "30967ad59df8fc604e92acc0f5254d90"
SRC_URI[sha256sum] = "435b80b23c593cbe1027db66da1b1757ca23a4a57b82cc0fb8676cf4d995fd81"

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
