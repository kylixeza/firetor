# Firebase Storage

- [Firebase Storage](#firebase-storage)
    * [Setup](#setup)
    * [Features](#features)
        + [Image](#image)
        + [Document](#document)


## Setup
- Enabled firebase storage in production level in your firebase project dashboard
- Go to `Rules` tab and  changes read/write permission to true
```groovy
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if true;
    }
  }
}
```
- Back to `Files` tab and look over the link that start with gs://

![image](https://user-images.githubusercontent.com/58837451/230731508-6ec39006-823f-49ac-9e78-a7af63b70a76.png)

- Take only link after gs://, so it would be like project-id.appspot.com

- Go to your code in `Firetor.kt` and put link as the paramter in `enableFirebaseStorage()`

```kotlin
fun Application.configureFiretor() {
    install(Firetor) {
        ...
        enableFirebaseStorage("project-id.appspot.com")
    }
}
```

## Features


### Image

>Supported image extension: `jpg`, `jpeg`, `png`, `gif`, `bmp`, `webp`, `tiff`

Example 1:

```kotlin
post("/image") {
    var url: String? = null
    val multipart = call.receiveMultipart()

    try {
        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                url = part.uploadImage()
            }
        }
        call.respond(HttpStatusCode.OK, url ?: "No image url fetched")
    } catch (e: Exception) {
        call.respondText("Error: ${e.message}")
    }
}
```

Example 2:

```kotlin
post("/image-with-params") {
    var url: String? = null
    val multipart = call.receiveMultipart()

    try {
        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                url = part.uploadImage(
                    "images",
                    ImageExtension.JPG,
                ) {
                    it.compress(0.5f)
                        .flipHorizontal()
                        .flipVertical()
                        .rotate(45.0)
                        .resize(100, 100)
                }
            }
        }
        call.respond(HttpStatusCode.OK, url ?: "No image url fetched")
    } catch (e: Exception) {
        call.respondText("Error: ${e.message}")
    }
}
```


|      Parameter       |                     Type                      |                                                      Usage                                                       |              Default Value               |        Example        |
|:--------------------:|:---------------------------------------------:|:----------------------------------------------------------------------------------------------------------------:|:----------------------------------------:|:---------------------:|
|        `path`        |                   `String`                    | To declare path of your image directory. If directory is null, it will be placed at default directory in storage |                  `null`                  |    `"user/avatar"`    |
|   `imageExtension`   |               `ImageExtension`                |                                        To declare extension of your image                                        | `ImageExtension.ORIGINAL_FILE_EXTENSION` | `ImageExtension.JPEG` |
| `imagePreprocessing` | `ImagePreprocessing.(ByteArray) -> ByteArray` |                                      To preprocessing image before uploaded                                      |                   `it`                   |  `it.compress(0.5f)`  |

Here some ImagePreprocessing functions:

|          Function          |   Parameters    |  Default Value  |                      Usage                       |
|:--------------------------:|:---------------:|:---------------:|:------------------------------------------------:|
|        `compress()`        |      Float      |        -        | To compress image quality (lossless compression) |
|         `rotate()`         |     Double      |        -        |      To rotate image (clockwise direction)       |
|         `resize()`         |    Int, Int     |      -, -       |            To resized image in pixels            |
|      `flipVertical()`      |        -        |                 |             To flip image vertically             |
|     `filpHorizontal()`     |        -        |                 |            To flip image horizontally            |
|         `rename()`         | String, Boolean |   -, `false`    |         To rename image to spesific name         |
| `renameWithOriginalName()` |     Boolean     |     `false`     |         To rename image to original name         |
|   `renameWithRandomId()`   |     Boolean     |     `false`     |           To rename image to random id           |

### Document

>Supported document extension: `pdf`, `doc`, `docx`, `xls`, `xlsx`, `ppt`, `pptx`, `txt`

Example :

```kotlin

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
```

|  Parameter   |                       Type                       |                                                        Usage                                                        |              Default Value               |               Example                |
|:------------:|:------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------:|:----------------------------------------:|:------------------------------------:|
|    `path`    |                     `String`                     | To declare path of your document directory. If directory is null, it will be placed at default directory in storage |                  `null`                  |           `"user/reports"`           |
| `preprocess` | `DocumentPreprocessing.(ByteArray) -> ByteArray` |                                      To preprocessing document before uploaded                                      |                   `it`                   | `it.renameWithOrignalFileNmae(true)` |

Here some DocumentPreprocessing functions:

|          Function          |   Parameters    |  Default Value  |                      Usage                       |
|:--------------------------:|:---------------:|:---------------:|:------------------------------------------------:|
|         `rename()`         | String, Boolean |   -, `false`    |         To rename image to spesific name         |
| `renameWithOriginalName()` |     Boolean     |     `false`     |         To rename image to original name         |
|   `renameWithRandomId()`   |     Boolean     |     `false`     |           To rename image to random id           |