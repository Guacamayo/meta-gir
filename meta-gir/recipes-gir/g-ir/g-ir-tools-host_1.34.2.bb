SUMMARY = "GObject Introspection Tools"
DESCRIPTION = "This package provides host side tools that can be run using \
QEMU (g-ir-compiler and g-ir-generate)"

include g-ir.inc

# This package is for use on the host only, and only stages files into
# native systroot; the target version of the tools is provided by the
# g-ir-core package, which we need in place
DEPENDS += "qemuwrapper-cross libgirepository"

SRC_URI += "file://g-ir-compiler		\
	    file://g-ir-generate		\
	    file://g-ir-ldd			\
	    file://g-ir-scanner			\
	    file://g-ir-runme			\
	    file://host-tools-only.patch	\
	   "

SCANNER_ENV = "PKG_CONFIG=${STAGING_DIR_NATIVE}${bindir_native}/pkg-config PKG_CONFIG_PATH=${PKG_CONFIG_PATH} PKG_CONFIG_LIBDIR=${PKG_CONFIG_LIBDIR}"
SCANNER_ARGS = "--library-path=${STAGING_DIR_HOST}${libdir} --add-include-path=${STAGING_DIR_TARGET}/${datadir}/gir-1.0"

COMPILER_ARGS = "--includedir=${STAGING_DIR_TARGET}${datadir}/gir-1.0"
GENERATE_ARGS = ""

# We have to build libgirepository because of how the makefiles are set up
# but we do not package it (we have libgirepository for that)
do_install_append () {
    rm -rf ${D}${libdir}
    rm -rf ${D}${includedir}
    rm -rf ${D}${datadir}/aclocal

    install -m 0755 ${WORKDIR}/g-ir-runme ${D}${bindir}/
    sed -i -e "s|SYSROOT|${STAGING_DIR_TARGET}|g" ${D}${bindir}/g-ir-runme

    install -m 0755 ${WORKDIR}/g-ir-ldd ${D}${bindir}/
    sed -i -e "s|SYSROOT|${STAGING_DIR_TARGET}|g" \
	      	${D}${bindir}/g-ir-ldd

    install -m 0755 ${WORKDIR}/g-ir-scanner ${D}${bindir}/
    sed -i -e "s|NATIVEROOT|${STAGING_DIR_NATIVE}|g" \
    	   -e "s|SYSROOT|${STAGING_DIR_TARGET}|g" \
    	   -e "s|INTROSPECTION_SCANNER_ENV=.*|INTROSPECTION_SCANNER_ENV=\"${SCANNER_ENV}\"|" \
    	   -e "s|INTROSPECTION_SCANNER_ARGS=.*|INTROSPECTION_SCANNER_ARGS=\"${SCANNER_ARGS}\"|" \
    	   -e "s|EXTRA_ARGS|${SCANNER_ARGS}|" \
	      	${D}${bindir}/g-ir-scanner
}

# Stage tools only into native sysroot
# Because normal packages do not stage binaries into target root, we install
# the binaries we need into the native root here with the .bin extension for
# use by the qemu wrapper we provide around them
MULTIMACH_TARGET_SYS = "${PACKAGE_ARCH}${HOST_VENDOR}-${HOST_OS}"
STAGING_BINDIR = "${STAGING_BINDIR_NATIVE}/${MULTIMACH_TARGET_SYS}"
do_populate_sysroot[sstate-inputdirs] = "${SYSROOT_DESTDIR}/${STAGING_DIR_NATIVE}/"
do_populate_sysroot[sstate-outputdirs] = "${STAGING_DIR_NATIVE}/"

SYSROOT_PREPROCESS_FUNCS += "gir_sysroot_preprocess"

gir_sysroot_preprocess () {
    sysroot_stage_dir ${D}${bindir} \
    		      ${SYSROOT_DESTDIR}/${STAGING_DIR_NATIVE}/${bindir}/${MULTIMACH_TARGET_SYS}

    for f in g-ir-compiler g-ir-generate
    do
	mv ${SYSROOT_DESTDIR}/${STAGING_DIR_NATIVE}/${bindir}/${MULTIMACH_TARGET_SYS}/$f \
	   ${SYSROOT_DESTDIR}/${STAGING_DIR_NATIVE}/${bindir}/${MULTIMACH_TARGET_SYS}/$f.bin
	install -m 0755 ${WORKDIR}/$f \
		   	${SYSROOT_DESTDIR}/${STAGING_DIR_NATIVE}/${bindir}/${MULTIMACH_TARGET_SYS}/$f
    	sed -i -e "s|XXXX|$f\.bin|g" -e "s|NATIVEBINDIR|${STAGING_DIR_NATIVE}/${bindir}/${MULTIMACH_TARGET_SYS}|g" \
	       -e "s|SYSROOT|${STAGING_DIR_TARGET}|g" \
	       -e "s|EXTRA_COMP_ARGS|${COMPILER_ARGS}|g" \
	       -e "s|EXTRA_GEN_ARGS|${GENERATE_ARGS}|g" \
	       	  	${SYSROOT_DESTDIR}/${STAGING_DIR_NATIVE}/${bindir}/${MULTIMACH_TARGET_SYS}/$f
    done
}

FILES_${PN}-dev += "${bindir} ${libdir} ${datadir}"
FILES_${PN} = ""
