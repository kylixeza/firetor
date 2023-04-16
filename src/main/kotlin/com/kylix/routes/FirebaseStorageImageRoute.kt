package com.kylix.routes

import com.kylix.image.FirebaseStorageImage.uploadImage
import com.kylix.image.ImageExtension
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.postImage() {

    post("/upload-original-image") {
        val multipart = call.receiveMultipart()
        var url: String? = null

        try {
            multipart.forEachPart {part ->
                if (part is PartData.FileItem) {
                    url = part.uploadImage {
                        it.compress(0.1f)
                    }
                }
                part.dispose()
            }
            call.respond(url ?: "No image uploaded")
        } catch (e: Exception) {
            call.respondText("Error: ${e.message}")
        }
    }

    post("/upload-image-renaming") {
        val multipart = call.receiveMultipart()
        var url: String? = null

        try {
            multipart.forEachPart {part ->
                if (part is PartData.FileItem) {
                    url = part.uploadImage(
                        path = "images",
                        fileExtension = ImageExtension.ORIGINAL_FILE_EXTENSION,
                        preprocessing = {
                            it.renameWithRandomId(true)
                                .compress(0.1f)
                                .renameWithOriginalName()
                                .rename("mas bro v2", true)
                        }
                    )
                }
                part.dispose()
            }
            call.respond(url ?: "No image uploaded")
        } catch (e: Exception) {
            call.respondText("Error: ${e.message}")
        }
    }

}