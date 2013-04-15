
META-GIR -- GObject Introspection for Yocto & OpenEmbedded
==========================================================


Introduction
------------

G-I is not an easy technology to support in a cross-compiling environment, as is
attested by the fact that even though g-i has been around for years, Yocto / OE
support is still missing. There are reasons for this: the technology per se is
cross-compilation unfriendly (i.e., g-i typelib is a raw dump of a C struct),
the tools are even more so, particularly the scanner (written in Python, but
using a custom python module, executing C compiler, ldd, and the compiled
product), and, to add an extra bit of spice, a single repository and build
system combining the tools, the consumer library and collection of typelibs.

The approach taken here is to use Qemu to allow running the tools in their
target environment, which, in spite of the obvious limitation (Qemu does not
support architecture XYZ I hear you say!), is the only viable approach at the
moment (other options, such as using IDL, have been discussed over the years,
but came to nought). Both gir and typelib generation is done under Qemu; in
principle the former could be done natively, but that would require building
far too many packages in their full native form, which is undesirable.

In order to work around the 'lump it all together' setup of the upstream
gobject-introspection project, four separate recipes are used to build g-i with
four different sets of patches and configuration:

 * libgirepository:   The primary package providing the consumer library for the
                      target (with the tools included in the dev sub package).

 * g-ir-core:         A collection of typelibs and gir files that are included
                      in the upstream gobject-introspection project for good
                      measure; for the target.

 * g-ir-tools-native: Provides the python-based tools, g-ir-scanner and
                      g-ir-annotation-tool, built for execution on the host. The
                      'native' designation is a half truth, the scanner runs the
                      C cross-compiler, uses ldd on the compilation product,
                      which it then executes. These steps are again facilitated
                      via Qemu and a set of wrapper scripts (which are, however,
                      provided by the next package).

 * g-ir-tools-host:   Provides the two C-based tools, g-ir-compiler and
                      g-ir-generate, built for the target, but, together with a
                      bunch of Qemu wrapper scripts staged into the native
                      multi-machine bin directory, since these are intended
                      solely for build-time execution on the host, even though
                      built for target.


Layer Organisation
------------------

The meta-gir repository consists of three separate layers, to make it easier to
pick and choose as required:

* meta-gir:       classes and packages that provide the tools and infrastructure
                  for g-i support,

* meta-gir-core:  bbappends that enable g-i for packages that are part of
                  oecore,

* meta-gir-extra: bbappends that enable g-i for packages that are part of
                  meta-gnome.


Enabling g-i in packages
------------------------

Package enabling is facilitated by the g-ir.bbclass. For simple and sensibly set
up upstream projects all that is required is to inherit g-ir (e.g., the
libsoup-2.4 bbappend). For more complex projects, that build multiple libraries
in different sub directories of their tree, it is also necessary to set up the
library loading paths (e.g., the gtk+3 bbappend). Some packages ship their own
version of the introspection autoconf macros; the g-ir class takes care of the
common case where the project contains m4/introspection.m4, but packages that
use other arrangements will require either a small configure fragment to remove
the offending file, or a patch (e.g., the pango and gst-plugins bbappends).

For ease of tweaking the g-i parameters, the following configuration variables
(with self-documenting names ;-) ) can be set in the recipes:

  * GIR_EXTRA_LIBS_PATH
  * GIR_EXTRA_COMPILER_ARGS
  * GIR_EXTRA_GENERATE_ARGS
  * GIR_EXTRA_RUNME_ARGS
  * GIR_EXTRA_SCANNER_ARGS

Of these, the first one is most often needed (and holds the usual, colon
separated, extra path to be used by the tools when resolving shared libs).


TODO
----

It's early days, and there is much to do ... at present what is in place is
enough to build core-image-sato. The long-term goal is to facilitate building
the whole of the Gnome 3 Desktop environment.


Known Issues
------------

 * Since it is currently impossible to extend the supported package sets, a
   small patch is needed for oecore image.bbclass to add the new gir-pkgs. This
   is located in the oecore-patches directory, and will hopefully be upstreamed
   at some point.

 * The scripts expect bash; if anybody feels strong enough about rewriting them,
   there is a free line awaiting in the AUTHORS file ...
