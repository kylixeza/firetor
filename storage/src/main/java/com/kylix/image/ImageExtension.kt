package com.kylix.image

/**
 * This class is used to specify the image extension
 * @param extension the extension of the image
 * @author Kylix Eza Saputra
 */
enum class ImageExtension(val extension: String) {
    ORIGINAL_FILE_EXTENSION("original_file_extension"),
    JPG("jpg"),
    PNG("png"),
    JPEG("jpeg"),
    GIF("gif"),
    BMP("bmp"),
    WEBP("webp")
}