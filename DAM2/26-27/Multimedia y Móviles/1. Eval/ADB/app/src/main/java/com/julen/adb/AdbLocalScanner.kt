package com.julen.adb

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

class AdbLocalScanner {

    /**
     * Escanea los puertos locales comunes de ADB sobre Wi-Fi.
     * Devuelve el puerto correcto si lo encuentra, o null si falla.
     */
    suspend fun findLocalAdbPort(): Int? = withContext(Dispatchers.IO) {
        // Rango de puertos estándar que Android asigna dinámicamente a ADB
        val startPort = 5555
        val endPort = 5585

        for (port in startPort..endPort) {
            if (isAdbPortOpen(port)) {
                return@withContext port // ¡Encontrado!
            }
        }
        return@withContext null
    }

    private fun isAdbPortOpen(port: Int): Boolean {
        return try {
            val socket = Socket()
            // Intentamos conectar con un tiempo de espera (timeout) muy corto (50ms) para ir rápido
            socket.connect(InetSocketAddress("127.0.0.1", port), 50)
            socket.close()
            true // El puerto respondió, hay algo escuchando ahí
        } catch (_: Exception) {
            false // Puerto cerrado
        }
    }
}
