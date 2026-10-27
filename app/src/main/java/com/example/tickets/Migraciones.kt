package com.example.tickets

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS tickets (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                titulo TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                categoria TEXT NOT NULL,
                ubicacion TEXT NOT NULL,
                estado TEXT NOT NULL,
                fechaCreacion INTEGER NOT NULL,
                solicitanteId INTEGER NOT NULL
            )
        """.trimIndent())
    }
}