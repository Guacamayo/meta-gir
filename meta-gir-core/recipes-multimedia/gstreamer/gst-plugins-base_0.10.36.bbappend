FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

GIR_EXTRA_LIBS_PATH = "../sdp/.libs:../pbutils/.libs:../interfaces/.libs"

inherit g-ir

include gst-plugins.inc

SRC_URI += "file://fix-pbutils-gir.patch"
