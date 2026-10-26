package com.example.tickets

import android.util.Base64
import java.security.MessageDigest
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

    fun verificar(contrasena: String, hashGuardado: String, saltGuardado: String): Boolean {
        val salt = Base64.decode(saltGuardado, Base64.NO_WRAP)
        val hash = Base64.decode(generarHash(contrasena, salt), Base64.NO_WRAP)
        val hashOriginal = Base64.decode(hashGuardado, Base64.NO_WRAP)
        return MessageDigest.isEqual(hash, hashOriginal)
    }
}