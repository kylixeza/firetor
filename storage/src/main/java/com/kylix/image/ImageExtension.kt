package com.kylix.image

/**
 * This class is used to define the extension of the image
 * @author Kylix Eza Saputra
 */
enum class ImageExtension(internal val extension: String) {
    ORIGINAL_FILE_EXTENSION("original_file_extension"),
    JPG("jpg"),
    PNG("png"),
    JPEG("jpeg"),
    GIF("gif"),
    BMP("bmp"),
    WEBP("webp")
}