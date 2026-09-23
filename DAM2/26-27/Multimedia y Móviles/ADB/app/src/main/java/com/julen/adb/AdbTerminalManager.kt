package com.julen.adb

import android.content.Context
import android.util.Base64
import android.util.Log
import com.cgutman.adblib.AdbBase64
import com.cgutman.adblib.AdbConnection
import com.cgutman.adblib.AdbCrypto
import com.cgutman.adblib.AdbStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.Socket

class AdbTerminalManager(private val host: String, private val port: Int) {

    private var adbConnection: AdbConnection? = null
    private var shellStream: AdbStream? = null

    private var onLineReadCallback: ((String) -> Unit)? = null

    fun setLecturaCallback(callback: (String) -> Unit) {
        this.onLineReadCallback = callback
    }

    /**
     * Inicia el handshake de ADB y abre el canal de Shell de la terminal.
     */
    suspend fun startTerminal(context: Context): Boolean = withContext(Dispatchers.IO) {
        try {
            val privKeyFile = File(context.filesDir, "adb_private.key")
            val pubKeyFile = File(context.filesDir, "adb_public.key")

            val base64 = AdbBase64 { b -> Base64.encodeToString(b, Base64.NO_WRAP) }

            val crypto = if (privKeyFile.exists() && pubKeyFile.exists()) {
                AdbCrypto.loadAdbKeyPair(base64, privKeyFile, pubKeyFile)
            } else {
                val c = AdbCrypto.generateAdbKeyPair(base64)
                c.saveAdbKeyPair(privKeyFile, pubKeyFile)
                c
            }

            val socket = Socket(host, port)
            adbConnection = AdbConnection.create(socket, crypto)

            Log.d("ADB_TERMINAL", "Conectando al demonio de ADB...")
            adbConnection!!.connect()
            Log.d("ADB_TERMINAL", "¡Conexión establecida y autorizada!")

            shellStream = adbConnection!!.open("shell:")

            // Bucle de escucha asíncrono con Corrutinas (reemplazando el hilo nativo de Java)
            CoroutineScope(Dispatchers.IO).launch {
                escucharRespuestasTerminal()
            }

            return@withContext true
        } catch (e: Exception) {
            Log.e("ADB_TERMINAL", "Error al abrir la terminal: ${e.message}")
            return@withContext false
        }
    }

    /**
     * Bucle continuo basado en Corrutinas que lee todo lo que la terminal responde.
     */
    private fun escucharRespuestasTerminal() {
        try {
            while (shellStream != null && !shellStream!!.isClosed) {
                val data = shellStream!!.read()
                if (data.isNotEmpty()) {
                    val textoRespuesta = String(data, Charsets.UTF_8)
                    Log.d("TERMINAL_OUTPUT", textoRespuesta)
                    onLineReadCallback?.invoke(textoRespuesta)
                }
            }
        } catch (e: Exception) {
            Log.e("TERMINAL_OUTPUT", "Canal de terminal cerrado: ${e.message}")
        }
    }

    /**
     * Permite enviar un comando de texto plano (ej: "ls\n")
     */
    suspend fun ejecutarComando(comando: String) = withContext(Dispatchers.IO) {
        try {
            shellStream?.let { stream ->
                if (!stream.isClosed) {
                    val comandoFormateado = if (comando.endsWith("\n")) comando else "$comando\n"
                    stream.write(comandoFormateado.toByteArray(Charsets.UTF_8))
                }
            }
        } catch (e: Exception) {
            Log.e("ADB_TERMINAL", "Error al escribir comando: ${e.message}")
        }
    }
}
