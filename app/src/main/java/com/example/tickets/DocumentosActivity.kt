package com.example.tickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class DocumentosActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Documentos") },
                        navigationIcon = {
                            IconButton(onClick = { finish() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                            }
                        }
                    )
                }
            ) { padding ->
                PantallaDocumentos(Modifier.padding(padding))
            }
        }
    }
}

@Composable
fun PantallaDocumentos(modifier: Modifier = Modifier) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()
    var mensaje by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    var eliminando by remember { mutableStateOf(false) }
    var documentos by remember { mutableStateOf(listOf<File>()) }
    var archivoEliminar by remember { mutableStateOf<File?>(null) }

    LaunchedEffect(Unit) {
        documentos = withContext(Dispatchers.IO) {
            listarDocumentos(contexto)
        }
    }

    val selector = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            cargando = true
            scope.launch {
                try {
                    val nombre = withContext(Dispatchers.IO) {
                        guardarDocumento(contexto, uri)
                    }
                    documentos = withContext(Dispatchers.IO) {
                        listarDocumentos(contexto)
                    }
                    mensaje = "Documento cargado: $nombre"
                } catch (e: Exception) {
                    mensaje = "No se pudo guardar el documento"
                } finally {
                    cargando = false
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp)
    ) {
        Text("Mis documentos")
        Spacer(Modifier.height(16.dp))
        Text("Selecciona un archivo PDF o Word")
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                selector.launch(arrayOf(
                    "application/pdf",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                ))
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando && !eliminando
        ) {
            Text(if (cargando) "Guardando..." else "Cargar documento")
        }

        Spacer(Modifier.height(16.dp))
        Text(mensaje)
        Spacer(Modifier.height(24.dp))
        Text("Documentos guardados")
        Spacer(Modifier.height(12.dp))

        if (documentos.isEmpty()) {
            Text("No hay documentos guardados")
        } else {
            documentos.forEach { archivo ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = nombreDocumento(archivo),
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = {
                            try {
                                abrirDocumento(contexto, archivo)
                            } catch (e: Exception) {
                                mensaje = "No se pudo abrir el documento"
                            }
                        },
                        enabled = !eliminando
                    ) {
                        Text("Ver")
                    }

                    Spacer(Modifier.width(6.dp))

                    OutlinedButton(
                        onClick = { archivoEliminar = archivo },
                        enabled = !eliminando
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }

    archivoEliminar?.let { archivo ->
        AlertDialog(
            onDismissRequest = {
                if (!eliminando) archivoEliminar = null
            },
            title = { Text("Eliminar documento") },
            text = {
                Text("¿Estás seguro de que deseas eliminar ${nombreDocumento(archivo)}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        eliminando = true
                        scope.launch {
                            try {
                                val eliminado = withContext(Dispatchers.IO) {
                                    eliminarDocumento(contexto, archivo)
                                }
                                if (eliminado) {
                                    documentos = withContext(Dispatchers.IO) {
                                        listarDocumentos(contexto)
                                    }
                                    mensaje = "Documento eliminado correctamente"
                                } else {
                                    mensaje = "No se pudo eliminar el documento"
                                }
                            } catch (e: Exception) {
                                mensaje = "Ocurrió un error al eliminar el documento"
                            } finally {
                                eliminando = false
                                archivoEliminar = null
                            }
                        }
                    },
                    enabled = !eliminando
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { archivoEliminar = null },
                    enabled = !eliminando
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}