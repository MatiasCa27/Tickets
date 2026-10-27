package com.example.tickets

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Usuario::class, Ticket::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDAO
    abstract fun ticketDao(): TicketDAO
}