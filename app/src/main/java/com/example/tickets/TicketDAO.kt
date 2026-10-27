package com.example.tickets

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TicketDAO {
    @Insert
    suspend fun insertar(ticket: Ticket): Long

    @Query("SELECT * FROM tickets ORDER BY fechaCreacion DESC")
    suspend fun listar(): List<Ticket>

    @Query("SELECT * FROM tickets WHERE solicitanteId = :usuarioId ORDER BY fechaCreacion DESC")
    suspend fun listarPorUsuario(usuarioId: Int): List<Ticket>

    @Query("SELECT * FROM tickets WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Ticket?

    @Query("UPDATE tickets SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String)
}