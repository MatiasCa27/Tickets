package com.example.tickets

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import kotlinx.coroutines.launch

class PerfilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val usuarioId = intent.getIntExtra("usuarioId", -1)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tickets.db"
        ).build()

        setContent {
            Scaffold { padding ->
                PantallaPerfil(
                    db.usuarioDao(),
                    usuarioId,
                    Modifier.padding(padding),
                    onVolver = { finish() },
                    onGuardado = { nombre ->
                        setResult(RESULT_OK, Intent().putExtra("nombre", nombre))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaPerfil(
    dao: UsuarioDAO,
    usuarioId: Int,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit,
    onGuardado: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var nombre by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(true) }
    var guardando by remember { mutableStateOf(false) }
    var encontrado by remember { mutableStateOf(false) }

    LaunchedEffect(usuarioId) {
        try {
            if (usuarioId <= 0) {
                mensaje = "Usuario no válido"
            } else {
                val usuario = dao.buscarPorId(usuarioId)
                if (usuario != null) {
                    nombre = usuario.nombre
                    cargo = usuario.cargo
                    encontrado = true
                } else {
                    mensaje = "Usuario no encontrado"
                }
            }
        } catch (e: Exception) {
            mensaje = "No se pudieron cargar los datos"
        } finally {
            cargando = false
        }
    }

    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp)) {
        Text("Editar datos de usuario")
        Spacer(Modifier.height(20.dp))

        if (cargando) {
            Text("Cargando datos...")
        } else {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = encontrado
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = cargo,
                onValueChange = { cargo = it },
                label = { Text("Cargo del funcionario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = encontrado
            )
            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (nombre.isBlank() || cargo.isBlank()) {
                        mensaje = "Complete todos los campos"
                    } else {
                        guardando = true
                        scope.launch {
                            try {
                                val nombreFinal = nombre.trim()
                                dao.actualizar(usuarioId, nombreFinal, cargo.trim())
                                onGuardado(nombreFinal)
                            } catch (e: Exception) {
                                mensaje = "No se pudieron guardar los cambios"
                            } finally {
                                guardando = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = encontrado && !guardando
            ) {
                Text(if (guardando) "Guardando..." else "Guardar cambios")
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(mensaje)
        Spacer(Modifier.height(20.dp))

        Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}