package com.kylix

/**
 * This class is used to specify the image extension
 * @param extension the extension of the image
 * @author Kylix Eza Saputra
 */
enum class ImageExtension(val extension: String) {
    JPG("jpg"),
    PNG("png"),
    JPEG("jpeg"),
    GIF("gif"),
    BMP("bmp"),
    WEBP("webp")
}