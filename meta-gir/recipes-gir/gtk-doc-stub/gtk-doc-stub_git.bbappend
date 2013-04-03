
do_install_append_class-target () {
    rm -rf ${D}${datadir}/gobject-introspection-1.0
    rm ${D}${datadir}/aclocal/introspection.m4
}