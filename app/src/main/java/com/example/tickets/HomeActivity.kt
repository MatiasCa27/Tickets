package com.example.tickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val nombre = intent.getStringExtra("nombre") ?: "Funcionario"
        setContent {
            Scaffold { padding ->
                PantallaPrincipal(nombre, Modifier.padding(padding)) { finish() }
            }
        }
    }
}

@Composable
fun PantallaPrincipal(nombre: String, modifier: Modifier = Modifier, onVolver: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("MPAZ Ticket")
        Spacer(Modifier.height(20.dp))
        Text("Bienvenido, $nombre")
        Spacer(Modifier.height(12.dp))
        Text("Sistema de solicitudes de mantención")
        Spacer(Modifier.height(30.dp))
        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}