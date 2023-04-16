package com.kylix.document

enum class DocumentContentType(
    internal val extension: String,
    internal val contentType: String
) {
    DOCX("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    DOC("doc","application/msword"),
    PDF("pdf","application/pdf"),
    XLSX("xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    XLS("xls","application/vnd.ms-excel"),
    PPTX("pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    PPT("ppt","application/vnd.ms-powerpoint"),
    TXT("txt","text/plain"),
}