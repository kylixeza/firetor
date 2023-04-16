package com.kylix.routes

import com.kylix.document.FirebaseStorageDocument.uploadDocument
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.postDocument() {
    post("/upload-document") {
        val multipart = call.receiveMultipart()
        var url: String? = null

        try {
            multipart.forEachPart {part ->
                if (part is PartData.FileItem) {
                    url = part.uploadDocument("documents") {
                        it.renameWithOriginalName(true)
                    }
                }
                part.dispose()
            }
            call.respond(url ?: "No document uploaded")
        } catch (e: Exception) {
            call.respondText("Error: ${e.message}")
        }
    }
}