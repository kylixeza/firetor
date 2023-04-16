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

/**
 * This class is used to preprocess the image before it is stored in the database
 * @author Kylix Eza Saputra
 */
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

    internal fun getFileName(): String = fileName

    /**
     * This function is used to compress quality of the image
     * @param quality is used to set the quality of the image
     * @return ByteArray
     * @author Kylix Eza Saputra
     */
    fun ByteArray.compress(quality: Float): ByteArray = run {
        var image = ImageIO.read(ByteArrayInputStream(this))
        val outputStream = ByteArrayOutputStream()
        val writer = ImageIO.getImageWritersByFormatName(extension).next()
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

    /**
     * This function is used to resize the image
     * @param newWidth is used to set the new width of the image
     * @param newHeight is used to set the new height of the image
     * @return ByteArray
     * @author Kylix Eza Saputra
     */
    fun ByteArray.resize(newWidth: Int, newHeight: Int): ByteArray {
        val inputStream = ByteArrayInputStream(this)
        val originalImage = ImageIO.read(inputStream)
        val resizedImage = originalImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH)
        val bufferedImage = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB)
        bufferedImage.graphics.drawImage(resizedImage, 0, 0, null)
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, extension, outputStream)
        return outputStream.toByteArray()
    }

    /**
     * This function is used to flip the image vertically
     * @return ByteArray
     * @author Kylix Eza Saputra
     */
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
        ImageIO.write(bufferedImage, extension, outputStream)
        outputStream.toByteArray()
    }

    /**
     * This function is used to flip the image horizontally
     * @return ByteArray
     * @author Kylix Eza Saputra
     */
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
        ImageIO.write(bufferedImage, extension, outputStream)
        outputStream.toByteArray()
    }

    /**
     * This function is used to rotate the image
     * @param degrees is used to set the degrees of the image
     * @return ByteArray
     * @author Kylix Eza Saputra
     */
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
        ImageIO.write(rotatedImage, extension, outputStream)
        return outputStream.toByteArray()
    }

    /**
     * This function is used to rename image file name with original name
     * @param includeTimeStamp is used to set whether the timestamp will be included or not
     * @return ByteArray
     * @author Kylix Eza Saputra
     */
    fun ByteArray.renameWithOriginalName(includeTimeStamp: Boolean = false): ByteArray {
        fileName = originalFileName?.substringBeforeLast(".") + if (includeTimeStamp) "_${System.currentTimeMillis()}.$extension" else ".$extension"
        return this
    }

    /**
     * This function is used to rename image file name with custom name
     * @param customName is used to set the custom name of the image
     * @param includeTimeStamp is used to set whether the timestamp will be included or not
     * @return ByteArray
     * @author Kylix Eza Saputra
     */
    fun ByteArray.rename(customName: String, includeTimeStamp: Boolean = false): ByteArray {
        fileName = customName + if (includeTimeStamp) "_${System.currentTimeMillis()}.$extension" else ".$extension"
        return this
    }

    /**
     * This function is used to rename image file name with random id
     * @param includeTimeStamp is used to set whether the timestamp will be included or not
     * @return ByteArray
     * @author Kylix Eza Saputra
     */
    fun ByteArray.renameWithRandomId(includeTimeStamp: Boolean = false): ByteArray {
        fileName = NanoIdUtils.randomNanoId() + if (includeTimeStamp) "_${System.currentTimeMillis()}.$extension" else ".$extension"
        return this
    }

}