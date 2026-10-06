package com.julen.adb

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(
    uiState: TerminalUiState,
    onSendCommand: (String) -> Unit
) {
    var commandInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto-scroll al final cada vez que llega una nueva línea de la terminal
    LaunchedEffect(uiState.logs.size) {
        if (uiState.logs.isNotEmpty()) {
            listState.animateScrollToItem(uiState.logs.size - 1)
        }
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("ADB Local Terminal", color = Color.White)
                        Text(
                            text = "Estado: ${uiState.statusMessage}",
                            color = if (uiState.isConnected) Color.Green else Color.Yellow,
                            fontSize = 11.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF111111))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF1E1E1E))
        ) {
            // 1. Área del Historial de la Consola con rendimiento optimizado
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                itemsIndexed(uiState.logs) { _, logLine ->
                    Text(
                        text = logLine,
                        color = Color(0xFF00FF00),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 2. Línea de Entrada de Comandos (incluye windowInsets para IME / teclado)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D0D0D))
                    .windowInsetsPadding(WindowInsets.ime)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$ ",
                    color = Color.Cyan,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(end = 4.dp)
                )

                TextField(
                    value = commandInput,
                    onValueChange = { commandInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe un comando...", color = Color.Gray) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.Green
                    ),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (commandInput.isNotBlank()) {
                                onSendCommand(commandInput)
                                commandInput = ""
                            }
                        }
                    )
                )

                Button(
                    onClick = {
                        if (commandInput.isNotBlank()) {
                            onSendCommand(commandInput)
                            commandInput = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333))
                ) {
                    Text("Enviar", color = Color.White)
                }
            }
        }
    }
}
