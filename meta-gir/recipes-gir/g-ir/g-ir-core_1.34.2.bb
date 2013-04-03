SUMMARY = "GObject Introspection Tools"
DESCRIPTION = "This package provides the tools for GObject introspection"

DEPENDS += "glib-2.0"

include g-ir.inc
inherit g-ir-base

SRC_URI += "file://configure-introspection.patch	\
	    file://gir-only.patch			\
	    file://fixup-gir-build.patch		\
	   "

SCANNER_ENV = "PKG_CONFIG=${STAGING_DIR_NATIVE}${bindir_native}/pkg-config PKG_CONFIG_PATH=${PKG_CONFIG_PATH} PKG_CONFIG_LIBDIR=${PKG_CONFIG_LIBDIR}"
SCANNER_ARGS = "--library-path=${STAGING_DIR_HOST}${libdir}"

do_install_append () {
    rm -rf ${D}${datadir}/aclocal
    rm -rf ${D}${datadir}/gobject-introspection-1.0
    rm -rf ${D}${datadir}/man
    rm -rf ${D}${libdir}/pkgconfig
}

do_configure_prepend () {
    sed -i -e "s|PYTHON=/usr/bin/python|PYTHON=${PYTHON}|" \
    	   -e "s|INTROSPECTION_SCANNER_ENV=.*|INTROSPECTION_SCANNER_ENV=\"${SCANNER_ENV}\"|" \
    	   -e "s|INTROSPECTION_SCANNER_ARGS=.*|INTROSPECTION_SCANNER_ARGS=\"${SCANNER_ARGS}\"|" \
    	   -e "s|GOBJECT_INTROSPECTION_LIBDIR=\"|GOBJECT_INTROSPECTION_LIBDIR=\"${STAGING_DIR_HOST}/|" \
        ${S}/configure.ac
}

export GIR_EXTRA_COMPILER_ARGS = "--includedir=./gir"
