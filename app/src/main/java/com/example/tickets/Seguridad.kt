package com.example.tickets

import android.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object Seguridad {
    fun generarHash(contrasena: String, salt: ByteArray): String {
        val spec = PBEKeySpec(contrasena.toCharArray(), salt, 600000, 256)
        return try {
            val hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).encoded
            Base64.encodeToString(hash, Base64.NO_WRAP)
        } finally {
            spec.clearPassword()
        }
    }
}