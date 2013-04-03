
PACKAGES_append_class-target = " ${PN}-gir"
DEPENDS_append_class-target = " g-ir-tools-host g-ir-tools-native libgirepository"

FILES_${PN}-gir += "${libdir}/girepository-1.0/*.typelib"
FILES_${PN}-dev += "${datadir}/gir-1.0/*.gir"

EXTRA_OECONF_append_class-target = " --enable-introspection"
EXTRA_OECONF_append_class-native = " --disable-introspection"
