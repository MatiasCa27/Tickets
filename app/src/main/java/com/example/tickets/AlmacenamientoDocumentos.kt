package com.example.tickets

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.util.Locale
import java.util.UUID

fun guardarDocumento(contexto: Context, uri: Uri): String {
    val nombre = contexto.contentResolver.query(uri, null, null, null, null)?.use {
        val indice = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (indice >= 0 && it.moveToFirst()) it.getString(indice) else null
    } ?: throw IOException("No se pudo obtener el nombre")

    val nombreSeguro = nombre.substringAfterLast('/').substringAfterLast('\\')
    val extension = nombreSeguro.substringAfterLast('.', "").lowercase(Locale.ROOT)

    if (extension !in listOf("pdf", "doc", "docx")) {
        throw IOException("Formato no permitido")
    }

    val carpeta = File(contexto.filesDir, "documentos")
    if (!carpeta.exists() && !carpeta.mkdirs()) {
        throw IOException("No se pudo crear la carpeta")
    }

    val archivo = File(carpeta, "${UUID.randomUUID()}_$nombreSeguro")

    try {
        val entrada = contexto.contentResolver.openInputStream(uri)
            ?: throw IOException("No se pudo abrir el documento")
        entrada.use {
            archivo.outputStream().use { salida ->
                it.copyTo(salida)
            }
        }
    } catch (e: Exception) {
        archivo.delete()
        throw e
    }

    return nombreSeguro
}

fun listarDocumentos(contexto: Context): List<File> {
    val carpeta = File(contexto.filesDir, "documentos")
    return carpeta.listFiles()
        ?.filter { it.isFile && it.extension.lowercase(Locale.ROOT) in listOf("pdf", "doc", "docx") }
        ?.sortedByDescending { it.lastModified() }
        ?: emptyList()
}

fun nombreDocumento(archivo: File): String {
    return archivo.name.substringAfter("_", archivo.name)
}

fun abrirDocumento(contexto: Context, archivo: File) {
    val tipo = when (archivo.extension.lowercase(Locale.ROOT)) {
        "pdf" -> "application/pdf"
        "doc" -> "application/msword"
        "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        else -> throw IllegalArgumentException("Formato no permitido")
    }

    val uri = FileProvider.getUriForFile(
        contexto,
        "${contexto.packageName}.fileprovider",
        archivo
    )

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, tipo)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    contexto.startActivity(Intent.createChooser(intent, "Abrir documento"))
}