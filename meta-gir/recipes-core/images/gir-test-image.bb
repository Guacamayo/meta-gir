DESCRIPTION = "Test image based on core-image-sato"

IMAGE_FEATURES += "splash package-management x11-base x11-sato ssh-server-dropbear hwcodecs"

LICENSE = "MIT"

inherit core-image

COMPLEMENTARY_GLOB[gir-pkgs] = "*-gir"

IMAGE_INSTALL += "python-pygobject"
IMAGE_FEATURES += "gir-pkgs"
