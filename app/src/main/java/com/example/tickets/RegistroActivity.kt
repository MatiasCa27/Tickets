package com.example.tickets

import android.os.Bundle
import android.util.Base64
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
import java.security.SecureRandom
import java.util.Locale

class RegistroActivity : ComponentActivity() {
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
                PantallaRegistro(db.usuarioDao(), Modifier.padding(padding)) { finish() }
            }
        }
    }
}

@Composable
fun PantallaRegistro(dao: UsuarioDAO, modifier: Modifier = Modifier, onVolver: () -> Unit) {
    val scope = rememberCoroutineScope()
    var nombre by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var guardando by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp)) {
        Text("Registro de usuarios")
        Spacer(Modifier.height(20.dp))

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))

        TextField(
            value = cargo,
            onValueChange = { cargo = it },
            label = { Text("Cargo del funcionario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))

        TextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))

        TextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))

        TextField(
            value = confirmar,
            onValueChange = { confirmar = it },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                when {
                    nombre.isBlank() || cargo.isBlank() || correo.isBlank() || contrasena.isBlank() || confirmar.isBlank() -> mensaje = "Complete todos los campos"
                    !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> mensaje = "Ingrese un correo válido"
                    contrasena.length < 8 -> mensaje = "La contraseña debe tener mínimo 8 caracteres"
                    contrasena != confirmar -> mensaje = "Las contraseñas no coinciden"
                    else -> {
                        val nombreFinal = nombre.trim()
                        val cargoFinal = cargo.trim()
                        val correoFinal = correo.trim().lowercase(Locale.ROOT)
                        val clave = contrasena
                        guardando = true
                        scope.launch {
                            try {
                                if (dao.buscarPorCorreo(correoFinal) != null) {
                                    mensaje = "Este correo ya está registrado"
                                } else {
                                    val salt = ByteArray(16)
                                    SecureRandom().nextBytes(salt)
                                    val hash = withContext(Dispatchers.Default) {
                                        Seguridad.generarHash(clave, salt)
                                    }
                                    dao.insertar(
                                        Usuario(
                                            nombre = nombreFinal,
                                            cargo = cargoFinal,
                                            correo = correoFinal,
                                            contrasenaHash = hash,
                                            salt = Base64.encodeToString(salt, Base64.NO_WRAP)
                                        )
                                    )
                                    nombre = ""
                                    cargo = ""
                                    correo = ""
                                    contrasena = ""
                                    confirmar = ""
                                    mensaje = "Usuario registrado correctamente"
                                }
                            } catch (e: Exception) {
                                mensaje = "No se pudo guardar el usuario"
                            } finally {
                                guardando = false
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !guardando
        ) {
            Text(if (guardando) "Guardando..." else "Registrarse")
        }

        Spacer(Modifier.height(12.dp))
        Text(mensaje)
        Spacer(Modifier.height(20.dp))

        Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}