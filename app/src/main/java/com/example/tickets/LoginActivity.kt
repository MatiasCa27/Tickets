package com.example.tickets

import android.os.Bundle
import android.util.Patterns
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tickets.db"
        ).build()

        setContent {
            Scaffold { padding ->
                PantallaLogin(db.usuarioDao(), Modifier.padding(padding)) { finish() }
            }
        }
    }
}

@Composable
fun PantallaLogin(dao: UsuarioDAO, modifier: Modifier = Modifier, onVolver: () -> Unit) {
    val scope = rememberCoroutineScope()
    var correo by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var usuarioActual by remember { mutableStateOf<String?>(null) }
    var verificando by remember { mutableStateOf(false) }


    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp)) {
        if (usuarioActual == null) {
            Text("Iniciar sesión")
            Spacer(Modifier.height(20.dp))

            TextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))

            TextField(
                value = contraseña,
                onValueChange = { contraseña = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    when {
                        correo.isBlank() || contraseña.isBlank() -> mensaje = "Complete todos los campos"
                        !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> mensaje = "Ingrese un correo válido"
                        else -> {
                            val correoFinal = correo.trim().lowercase(Locale.ROOT)
                            val clave = contraseña
                            verificando = true
                            mensaje = ""
                            scope.launch {
                                try {
                                    val usuario = dao.buscarPorCorreo(correoFinal)
                                    val valido = usuario != null && withContext(Dispatchers.Default) {
                                        Seguridad.verificar(clave, usuario.contraseñaHash, usuario.salt)
                                    }
                                    if (valido && usuario != null) {
                                        usuarioActual = usuario.nombre
                                        contraseña = ""
                                    } else {
                                        mensaje = "Correo o contraseña incorrectos"
                                    }
                                } catch (e: Exception) {
                                    mensaje = "No se pudo consultar la base de datos"
                                } finally {
                                    verificando = false
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !verificando
            ) {
                Text(if (verificando) "Verificando..." else "Iniciar sesión")
            }

            Spacer(Modifier.height(12.dp))
            Text(mensaje)
        } else {
            Text("Bienvenido, $usuarioActual")
            Spacer(Modifier.height(20.dp))
            Text("Has iniciado sesión correctamente")
            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    usuarioActual = null
                    correo = ""
                    contraseña = ""
                    mensaje = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }

        Spacer(Modifier.height(20.dp))
        Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}