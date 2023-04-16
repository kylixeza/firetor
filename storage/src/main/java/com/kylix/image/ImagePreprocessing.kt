package com.kylix.image

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class ImagePreprocessing(
    private val imageExtension: ImageExtension,
    private val isImagePortrait: Boolean,
    private val originalFileName: String?,
) {

    private val extension = if (imageExtension == ImageExtension.ORIGINAL_FILE_EXTENSION) {
        originalFileName?.substringAfterLast(".") ?: "jpg"
    } else {
        imageExtension.extension
    }

    private var fileName = NanoIdUtils.randomNanoId() + ".$extension"

    fun getFileName(): String = fileName

    fun ByteArray.compress(quality: Float): ByteArray = run {
        var image = ImageIO.read(ByteArrayInputStream(this))
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
        outputStream.toByteArray()
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

        if (isImagePortrait) {
            bufferedImage.graphics.drawImage(originalImage, 0, 0, originalImage.width, originalImage.height, originalImage.width, 0, 0, originalImage.height, null)
        } else {
            bufferedImage.graphics.drawImage(originalImage, 0, 0, originalImage.width, originalImage.height, 0, originalImage.height, originalImage.width, 0, null)
        }

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, imageExtension.extension, outputStream)
        outputStream.toByteArray()
    }

    fun ByteArray.flipHorizontal(): ByteArray = run {
        val inputStream = ByteArrayInputStream(this)
        val originalImage = ImageIO.read(inputStream)
        val bufferedImage = BufferedImage(originalImage.width, originalImage.height, BufferedImage.TYPE_INT_RGB)

        if (isImagePortrait) {
            bufferedImage.graphics.drawImage(originalImage, 0, 0, originalImage.width, originalImage.height, 0, originalImage.height, originalImage.width, 0, null)
        } else {
            bufferedImage.graphics.drawImage(originalImage, 0, 0, originalImage.width, originalImage.height, originalImage.width, 0, 0, originalImage.height, null)
        }

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, imageExtension.extension, outputStream)
        outputStream.toByteArray()
    }

    fun ByteArray.rotate(degrees: Double): ByteArray = run {
        val inputStream = ByteArrayInputStream(this)
        val originalImage = ImageIO.read(inputStream)

        val radians = Math.toRadians(degrees)
        val sin = abs(sin(radians))
        val cos = abs(cos(radians))
        val rotatedWidth = (originalImage.width * cos + originalImage.height * sin).toInt()
        val rotatedHeight = (originalImage.height * cos + originalImage.width * sin).toInt()

        val rotatedImage = BufferedImage(rotatedWidth, rotatedHeight, BufferedImage.TYPE_INT_RGB)
        val graphics = rotatedImage.graphics as Graphics2D
        graphics.transform = AffineTransform.getRotateInstance(radians, rotatedWidth / 2.0, rotatedHeight / 2.0)
        graphics.drawImage(originalImage, (rotatedWidth - originalImage.width) / 2, (rotatedHeight - originalImage.height) / 2, null)
        graphics.dispose()

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(rotatedImage, imageExtension.extension, outputStream)
        return outputStream.toByteArray()
    }

    fun ByteArray.renameWithOriginalName(includeTimeStamp: Boolean = false): ByteArray {
        fileName = originalFileName?.substringBeforeLast(".") + if (includeTimeStamp) "_${System.currentTimeMillis()}.$extension" else ".$extension"
        return this
    }

    fun ByteArray.rename(customName: String, includeTimeStamp: Boolean = false): ByteArray {
        fileName = customName + if (includeTimeStamp) "_${System.currentTimeMillis()}.$extension" else ".$extension"
        return this
    }

    fun ByteArray.renameWithRandomId(includeTimeStamp: Boolean = false): ByteArray {
        fileName = NanoIdUtils.randomNanoId() + if (includeTimeStamp) "_${System.currentTimeMillis()}.$extension" else ".$extension"
        return this
    }

}