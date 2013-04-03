SUMMARY = "GObject Introspection Tools"
DESCRIPTION = "This is the target package, containing the tools and \
libgirepository"

include g-ir.inc

DEPENDS += "python"
RDEPENDS_${PN} += "g-ir-core-gir"

SRC_URI += "file://no-gir.patch"

do_install_append () {
    rm ${D}${libdir}/gobject-introspection/giscanner/*.la
    rm ${D}${libdir}/gobject-introspection/giscanner/*.a
    install -d ${D}${datadir}/gobject-introspection-1.0
    install ${S}/girepository/gdump.c ${D}${datadir}/gobject-introspection-1.0/
    install ${S}/Makefile.introspection ${D}${datadir}/gobject-introspection-1.0/
}

SYSROOT_PREPROCESS_FUNCS += "gir_sysroot_preprocess"
gir_sysroot_preprocess () {
    sysroot_stage_dir ${D}${datadir} \
    		      ${SYSROOT_DESTDIR}/${datadir}
    sed -i -e "s|INTROSPECTION_GIRDIR=\`|INTROSPECTION_GIRDIR=${STAGING_DIR_HOST}/\`|" \
    	   -e "s|INTROSPECTION_TYPELIBDIR=\"|INTROSPECTION_TYPELIBDIR=\"${STAGING_DIR_HOST}/|" \
	   -e "s|INTROSPECTION_MAKEFILE=\`|INTROSPECTION_MAKEFILE=${STAGING_DIR_HOST}/\`|" \
	   -e "s|INTROSPECTION_SCANNER=.*|INTROSPECTION_SCANNER=\`which g-ir-scanner\`|" \
	   -e "s|INTROSPECTION_COMPILER=.*|INTROSPECTION_COMPILER=\`which g-ir-compiler\`|" \
	   -e "s|INTROSPECTION_GENERATE=.*|INTROSPECTION_GENERATE=\`which g-ir-generate\`|" \
        ${SYSROOT_DESTDIR}/${datadir}/aclocal/introspection.m4
}

FILES_${PN}-dbg += "${libdir}/gobject-introspection/giscanner/.debug"
FILES_${PN}-dev += "${bindir} ${datadir} \
		    ${libdir}/gobject-introspection/giscanner"
