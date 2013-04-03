FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://fix-introspection.patch"

inherit g-ir
