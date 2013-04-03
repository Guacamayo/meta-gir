HOMEPAGE = "http://gnome.org"
BUGTRACKER = "https://bugzilla.gnome.org/"
SECTION = "libs"

DEPENDS = "glib-2.0 libffi python-native"
LICENSE = "LGPLv2+ & GPLv2+"

LIC_FILES_CHKSUM = "file://COPYING;md5=90d577535a3898e1ae5dbf0ae3509a8c \
                    file://tools/compiler.c;endline=20;md5=fc5007fc20022720e6c0b0cdde41fabd \
                    file://giscanner/sourcescanner.c;endline=22;md5=194d6e0c1d00662f32d030ce44de8d39 \
                    file://girepository/giregisteredtypeinfo.c;endline=21;md5=661847611ae6979465415f31a759ba27"

inherit autotools pkgconfig gtk-doc gnome

SRCNAME="gobject-introspection"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/${SRCNAME}/${@gnome_verdir("${PV}")}/${SRCNAME}-${PV}.tar.xz \
	   file://configure-common.patch \
	  "
SRC_URI[md5sum] = "9dc9822eb2912e9b73e51ae67fe86145"
SRC_URI[sha256sum] = "bc7948a35ce2218922dfb0806679cad21060715cc0c340cf7823eb633cc03429"

S = "${WORKDIR}/${SRCNAME}-${PV}"

export PKG_CONFIG_FOR_BUILD = "${STAGING_BINDIR_NATIVE}/pkg-config-native"

do_configure_prepend () {
    sed -i "s|GLIB_INCLUDEDIR=\`|GLIB_INCLUDEDIR=${STAGING_DIR_HOST}/\`|" \
        ${S}/configure.ac
    sed -i "s|GLIB_LIBDIR=\`|GLIB_LIBDIR=${STAGING_DIR_HOST}/\`|" \
        ${S}/configure.ac
    sed -i "s|GOBJECT_INCLUDEDIR=\`|GOBJECT_INCLUDEDIR=${STAGING_DIR_HOST}/\`|" \
        ${S}/configure.ac
    sed -i "s|GOBJECT_LIBDIR=\`|GOBJECT_LIBDIR=${STAGING_DIR_HOST}/\`|" \
        ${S}/configure.ac
    sed -i "s|GMODULE_INCLUDEDIR=\`|GMODULE_INCLUDEDIR=${STAGING_DIR_HOST}/\`|" \
        ${S}/configure.ac
    sed -i "s|GMODULE_LIBDIR=\`|GMODULE_LIBDIR=${STAGING_DIR_HOST}/\`|" \
        ${S}/configure.ac
    sed -i "s|GIO_INCLUDEDIR=\`|GIO_INCLUDEDIR=${STAGING_DIR_HOST}/\`|" \
        ${S}/configure.ac
    sed -i "s|GIO_LIBDIR=\`|GIO_LIBDIR=${STAGING_DIR_HOST}/\`|" \
        ${S}/configure.ac
    sed -i "s|GIO_UNIX_HDRS=\`|GIO_UNIX_HDRS=${STAGING_DIR_HOST}/\`|" \
        ${S}/configure.ac

    sed -i -e "s|INTROSPECTION_GIRDIR=\`|INTROSPECTION_GIRDIR=${STAGING_DIR_HOST}/\`|" \
    	   -e "s|INTROSPECTION_TYPELIBDIR=\`|INTROSPECTION_TYPELIBDIR=${STAGING_DIR_HOST}/\`|" \
	   -e "s|INTROSPECTION_MAKEFILE=\`|INTROSPECTION_MAKEFILE=${STAGING_DIR_HOST}/\`|" \
	   -e "s|INTROSPECTION_SCANNER=.*|INTROSPECTION_SCANNER=g-ir-scanner|" \
	   -e "s|INTROSPECTION_COMPILER=.*|INTROSPECTION_COMPILER=g-ir-compiler|" \
	   -e "s|INTROSPECTION_GENERATE=.*|INTROSPECTION_GENERATE=g-ir-generate|" \
        ${S}/configure.ac
}

