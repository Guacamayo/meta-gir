SUMMARY = "GObject Introspection Tools"
DESCRIPTION = "This package provides native verion g-ir-scanner for use on host"

include g-ir.inc

inherit native pythonnative

DEPENDS += "prelink-native"

SRC_URI += "file://scanner-only.patch		\
	    file://use-runme-wrapper.patch	\
	    file://use-ldd-wrapper.patch	\
	   "

# Need to set up the scanner so that it uses the target root
SCANNER_ENV = "PKG_CONFIG=${STAGING_DIR_NATIVE}${bindir_native}/pkg-config PKG_CONFIG_PATH=${PKG_CONFIG_PATH} PKG_CONFIG_LIBDIR=${PKG_CONFIG_LIBDIR}"
SCANNER_ARGS = "--library-path=${STAGING_DIR_HOST}${libdir}"

do_install_append () {
    rm ${D}${libdir}/gobject-introspection/giscanner/*.la
    rm ${D}${libdir}/gobject-introspection/giscanner/*.a

    # These files are provided by gtk-doc-stub-native, and we only want the
    # stubs for native packages, so do not try to replace them
    rm -rf ${D}${datadir}/aclocal
    rm ${D}${datadir}/gobject-introspection-1.0/Makefile.introspection

    install -d ${D}${datadir}/gobject-introspection-1.0
    install ${S}/girepository/gdump.c ${D}${datadir}/gobject-introspection-1.0/

    mv ${D}${bindir}/g-ir-scanner ${D}${bindir}/g-ir-scanner.py
}

do_configure_prepend () {
    sed -i -e "s|PYTHON=/usr/bin/python|PYTHON=${PYTHON}|" \
    	   -e "s|INTROSPECTION_SCANNER_ENV=.*|INTROSPECTION_SCANNER_ENV=\"${SCANNER_ENV}\"|" \
    	   -e "s|INTROSPECTION_SCANNER_ARGS=.*|INTROSPECTION_SCANNER_ARGS=\"${SCANNER_ARGS}\"|" \
        ${S}/configure.ac
}

SYSROOT_PREPROCESS_FUNCS += "gir_sysroot_preprocess"

gir_sysroot_preprocess () {
    sysroot_stage_dir ${D}${datadir} \
    		      ${SYSROOT_DESTDIR}/${STAGING_DIR_NATIVE}/${datadir}
}
