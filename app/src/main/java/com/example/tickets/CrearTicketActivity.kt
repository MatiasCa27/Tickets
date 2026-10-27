package com.example.tickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class CrearTicketActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Crear ticket") },
                        navigationIcon = {
                            IconButton(onClick = { finish() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                            }
                        }
                    )
                }
            ) { padding ->
                FormularioTicket(Modifier.padding(padding))
            }
        }
    }
}

@Composable
fun FormularioTicket(modifier: Modifier = Modifier) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var expandido by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }
    val categorias = listOf("Infraestructura", "Tecnología", "Mobiliario", "Otra")

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Nueva solicitud de mantención")

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título del problema") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text("Categoría")
        Box {
            OutlinedButton(
                onClick = { expandido = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (categoria.isEmpty()) "Seleccionar categoría" else categoria)
            }
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                categorias.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            categoria = opcion
                            expandido = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = ubicacion,
            onValueChange = { ubicacion = it },
            label = { Text("Ubicación del problema") },
            placeholder = { Text("Ejemplo: Sala 5") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción del problema") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                mensaje = when {
                    titulo.isBlank() -> "Ingrese el título del problema"
                    categoria.isBlank() -> "Seleccione una categoría"
                    ubicacion.isBlank() -> "Ingrese la ubicación"
                    descripcion.isBlank() -> "Ingrese la descripción"
                    else -> "Formulario válido. Pendiente guardar el ticket"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Validar ticket")
        }

        Text(mensaje)
    }
}