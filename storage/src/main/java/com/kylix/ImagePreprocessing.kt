package com.kylix

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class ImagePreprocessing(private val imageExtension: ImageExtension) {

    fun ByteArray.compress(quality: Float): ByteArray = run {
        var image = ImageIO.read(ByteArrayInputStream(this))
        val isRawImagePortrait = this.isImagePortrait()
        val outputStream = ByteArrayOutputStream()
        val writer = ImageIO.getImageWritersByFormatName(imageExtension.extension).next()
        val params = writer.defaultWriteParam
        params.compressionMode = javax.imageio.ImageWriteParam.MODE_EXPLICIT
        params.compressionQuality = quality
        val imageOutputStream = ImageIO.createImageOutputStream(outputStream)
        writer.output = imageOutputStream

        image = if (image.colorModel.hasAlpha()) {
            image.removeAlphaChannel()
        } else {
            image
        }

        writer.write(null, javax.imageio.IIOImage(image, null, null), params)
        imageOutputStream.close()
        val compressedImage = outputStream.toByteArray()

        if (isRawImagePortrait) {
            compressedImage.rotate(90.0)
        } else {
            compressedImage
        }
    }
    private fun BufferedImage.removeAlphaChannel(): BufferedImage {
        if (!colorModel.hasAlpha()) {
            return createImage(width, height, true)
        }
        val target: BufferedImage = createImage(width, height, false)
        val g = target.createGraphics()
        g.fillRect(0, 0, width, height)
        g.drawImage(this, 0, 0, null)
        g.dispose()
        return target
    }

    private fun createImage(width: Int, height: Int, hasNoAlpha: Boolean) = run {
        BufferedImage(width, height, if (hasNoAlpha) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB)
    }

    private fun ByteArray.isImagePortrait(): Boolean {
        val inputStream = ByteArrayInputStream(this)
        val originalImage = ImageIO.read(inputStream)
        return originalImage.height > originalImage.width
    }

    fun ByteArray.resize(newWidth: Int, newHeight: Int): ByteArray {
        val inputStream = ByteArrayInputStream(this)
        val originalImage = ImageIO.read(inputStream)
        val resizedImage = originalImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH)
        val bufferedImage = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB)
        bufferedImage.graphics.drawImage(resizedImage, 0, 0, null)
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, imageExtension.extension, outputStream)
        return outputStream.toByteArray()
    }

    fun ByteArray.flipVertical(): ByteArray = run {
        val inputStream = ByteArrayInputStream(this)
        val originalImage = ImageIO.read(inputStream)
        val bufferedImage = BufferedImage(originalImage.width, originalImage.height, BufferedImage.TYPE_INT_RGB)
        bufferedImage.graphics.drawImage(originalImage, 0, 0, originalImage.width, originalImage.height, 0, originalImage.height, originalImage.width, 0, null)
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, imageExtension.extension, outputStream)
        return@run outputStream.toByteArray()
    }

    fun ByteArray.rotate(degrees: Double): ByteArray = run {
        val inputStream = ByteArrayInputStream(this)
        val originalImage = ImageIO.read(inputStream)
        val bufferedImage = BufferedImage(originalImage.width, originalImage.height, BufferedImage.TYPE_INT_RGB)
        val g = bufferedImage.createGraphics()
        g.rotate(Math.toRadians(degrees), originalImage.width / 2.0, originalImage.height / 2.0)
        g.drawImage(originalImage, null, 0, 0)
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, imageExtension.extension, outputStream)
        return@run outputStream.toByteArray()

    }

    fun ByteArray.flipHorizontal(): ByteArray = run {
        val inputStream = ByteArrayInputStream(this)
        val originalImage = ImageIO.read(inputStream)
        val bufferedImage = BufferedImage(originalImage.width, originalImage.height, BufferedImage.TYPE_INT_RGB)
        bufferedImage.graphics.drawImage(originalImage, 0, 0, originalImage.width, originalImage.height, originalImage.width, 0, 0, originalImage.height, null)
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, imageExtension.extension, outputStream)
        return@run outputStream.toByteArray()
    }

}