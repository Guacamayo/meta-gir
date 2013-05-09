
inherit g-ir

do_install_append () {
    install -d ${D}${libdir}/girepository-1.0/
    mv ${D}${libdir}/mutter/*.typelib ${D}${libdir}/girepository-1.0/

    install -d ${D}${datadir}/gir-1.0/
    mv ${D}${libdir}/mutter/*.gir ${D}${datadir}/gir-1.0/
}