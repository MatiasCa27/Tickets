package com.example.tickets

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios", indices = [Index(value = ["correo"], unique = true)])
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val cargo: String,
    val correo: String,
    val contrasenaHash: String,
    val salt: String,
    val rol: String = "SOLICITANTE"
)