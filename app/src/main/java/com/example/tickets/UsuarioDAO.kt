package com.example.tickets

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDAO {
    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuarios")
    suspend fun listar(): List<Usuario>

    @Query("UPDATE usuarios SET nombre = :nombre, cargo = :cargo WHERE id = :id")
    suspend fun actualizar(id: Int, nombre: String, cargo: String)

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Usuario?
}