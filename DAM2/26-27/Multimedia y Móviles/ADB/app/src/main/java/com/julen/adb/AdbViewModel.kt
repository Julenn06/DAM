package com.julen.adb

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdbViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(TerminalUiState())
    val uiState: StateFlow<TerminalUiState> = _uiState.asStateFlow()

    private var terminalManager: AdbTerminalManager? = null

    // Canal de alta velocidad para procesar líneas de log sin bloquear ni saturar el hilo principal
    private val logChannel = Channel<String>(Channel.UNLIMITED)

    init {
        initAdbTerminal()
        startLogBatchProcessor()
    }

    private fun initAdbTerminal() {
        viewModelScope.launch {
            agregarLineaLog("Buscando puerto ADB local...")

            val localScanner = AdbLocalScanner()
            val activePort = localScanner.findLocalAdbPort()

            if (activePort != null) {
                agregarLineaLog("Puerto detectado en localhost:$activePort")
                agregarLineaLog("Abriendo canal seguro...")

                terminalManager = AdbTerminalManager("127.0.0.1", activePort)

                val conectado = terminalManager!!.startTerminal(getApplication())

                if (conectado) {
                    agregarLineaLog("¡Conectado con éxito a la Shell de Android!")
                    _uiState.update { it.copy(isConnected = true, statusMessage = "Conectado") }

                    terminalManager!!.setLecturaCallback { textoNuevo ->
                        textoNuevo.split("\n").forEach { linea ->
                            if (linea.isNotBlank() || linea == "") {
                                logChannel.trySend(linea)
                            }
                        }
                    }
                } else {
                    agregarLineaLog("Error: No se pudo verificar la firma criptográfica.")
                    _uiState.update { it.copy(statusMessage = "Error de firma") }
                }
            } else {
                agregarLineaLog("Error: No se encontró el puerto. Verifica que la 'Depuración por Wi-Fi' esté activa.")
                _uiState.update { it.copy(statusMessage = "Puerto no encontrado") }
            }
        }
    }

    /**
     * Procesador en lotes (batching) optimizado para FPS máximos.
     * Agrupa múltiples líneas entrantes y actualiza la UI en lotes para evitar caídas de fotogramas (jank).
     * Además, limita el historial a 2000 líneas para mantener el rendimiento del LazyColumn óptimo.
     */
    private fun startLogBatchProcessor() {
        viewModelScope.launch(Dispatchers.Default) {
            val batch = mutableListOf<String>()
            try {
                logChannel.consumeAsFlow().collect { line ->
                    batch.add(line)
                    while (true) {
                        val next = logChannel.tryReceive().getOrNull() ?: break
                        batch.add(next)
                        if (batch.size >= 100) break
                    }

                    if (batch.isNotEmpty()) {
                        val linesToAdd = batch.toList()
                        batch.clear()

                        withContext(Dispatchers.Main) {
                            _uiState.update { state ->
                                val newLogs = (state.logs + linesToAdd)
                                val limitedLogs =
                                    if (newLogs.size > 2000) newLogs.takeLast(2000) else newLogs
                                state.copy(logs = limitedLogs)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ADB_VIEWMODEL", "Error en procesador de logs: ${e.message}")
            }
        }
    }

    fun enviarComando(comando: String) {
        if (comando.isBlank()) return

        _uiState.update { state ->
            val newLogs = (state.logs + "$ $comando")
            val limitedLogs = if (newLogs.size > 2000) newLogs.takeLast(2000) else newLogs
            state.copy(logs = limitedLogs)
        }

        viewModelScope.launch {
            terminalManager?.ejecutarComando(comando)
        }
    }

    private fun agregarLineaLog(mensaje: String) {
        Log.d("ADB_VIEWMODEL", mensaje)
        _uiState.update { state ->
            val newLogs = (state.logs + "[SISTEMA]: $mensaje")
            val limitedLogs = if (newLogs.size > 2000) newLogs.takeLast(2000) else newLogs
            state.copy(logs = limitedLogs)
        }
    }
}

data class TerminalUiState(
    val logs: List<String> = emptyList(),
    val isConnected: Boolean = false,
    val statusMessage: String = "Desconectado"
)
