package com.julen.socios.ui.sheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.julen.socios.model.BonoRegaloInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BonoRegaloSheet(
    semanaKey: String,
    bonoInfo: BonoRegaloInfo?,
    onDismiss: () -> Unit,
    onSaveBono: (BonoRegaloInfo) -> Unit,
    onDeleteBono: (String) -> Unit,
    sheetState: SheetState
) {
    var esFijo by remember { mutableStateOf(bonoInfo?.esMontoFijo ?: true) }
    var montoText by remember { mutableStateOf(bonoInfo?.monto?.toString() ?: "140") }
    var notaText by remember { mutableStateOf(bonoInfo?.nota ?: "Ajuste manual") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "🎁 Ajuste de Bonus / Bonus Regalo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(selected = esFijo, onClick = { esFijo = true })
                Text("Monto Fijo (remplaza el bonus automático)")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(selected = !esFijo, onClick = { esFijo = false })
                Text("Monto Extra (se suma al bonus automático)")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = montoText,
                onValueChange = { montoText = it },
                label = { Text("Importe del Bonus (€)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = notaText,
                onValueChange = { notaText = it },
                label = { Text("Nota / Motivo del ajuste") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                if (bonoInfo != null && bonoInfo.activo) {
                    OutlinedButton(
                        onClick = {
                            onDeleteBono(semanaKey)
                            onDismiss()
                        }
                    ) {
                        Text("Restablecer Automático")
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        val monto = montoText.toDoubleOrNull() ?: 0.0
                        onSaveBono(
                            BonoRegaloInfo(
                                semanaKey = semanaKey,
                                activo = true,
                                esMontoFijo = esFijo,
                                monto = monto,
                                nota = notaText.trim()
                            )
                        )
                        onDismiss()
                    }
                ) {
                    Text("Guardar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
