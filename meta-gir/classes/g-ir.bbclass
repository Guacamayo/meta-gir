
inherit g-ir-base
DEPENDS_append_class-target += " g-ir-core"

export GIR_EXTRA_LIBS_PATH
export GIR_EXTRA_COMPILER_ARGS
export GIR_EXTRA_GENERATE_ARGS
export GIR_EXTRA_RUNME_ARGS
export GIR_EXTRA_SCANNER_ARGS

# For some reason, autoconf is looking into the native sysroot before the
# target one, and is picking up the introspection.m4 file from gtk-doc-stub
# (we need both, so that native packages would build as well, so cp the correct
# version into the m4 subdir
#
# Another common problem in the introspection recipes is hardcoded libtool
# name in the --libtool argument, so do a global search and replace (admitedly,
# this is slow, but it saves huge amount of patching.
#
do_configure_prepend_class-target () {
    mkdir -p ${S}/m4
    cp ${STAGING_DIR_TARGET}/${datadir}/aclocal/introspection.m4 ${S}/m4

    find ${S} -type f -name Makefile.am -print0 | xargs -0 -n 1 \
        sed -i -e 's|--libtool=\"$(top_builddir)/libtool\"|--libtool=\"$(LIBTOOL)\"|g'
}