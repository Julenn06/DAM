package com.julen.socios.ui.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.julen.socios.model.Socio
import com.julen.socios.ui.theme.OrangePending
import com.julen.socios.util.CalculoComisiones
import com.julen.socios.util.toFormattedEuros

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddSocioSheet(
    semanaKey: String,
    defaultDiaId: Int,
    socioToEdit: Socio?,
    onDismiss: () -> Unit,
    onSave: (Socio) -> Unit,
    sheetState: SheetState
) {
    var selectedAmount by remember { mutableStateOf(socioToEdit?.colaboracion ?: 20.0) }
    var customAmountText by remember {
        mutableStateOf(
            if (socioToEdit != null && socioToEdit.colaboracion !in listOf(10.0, 12.0, 15.0, 18.0, 20.0, 25.0, 30.0)) {
                socioToEdit.colaboracion.toString()
            } else ""
        )
    }
    var selectedDiaId by remember { mutableIntStateOf(socioToEdit?.diaSemanaId ?: defaultDiaId) }
    var isHecho by remember { mutableStateOf(socioToEdit?.hecho ?: false) }
    var nombreText by remember { mutableStateOf(socioToEdit?.nombreSocio ?: "") }
    var notasText by remember { mutableStateOf(socioToEdit?.notas ?: "") }

    val cuotasPredeterminadas = listOf(10.0, 12.0, 15.0, 18.0, 20.0, 25.0, 30.0)

    val currentCuota = if (customAmountText.isNotBlank()) {
        customAmountText.toDoubleOrNull() ?: selectedAmount
    } else {
        selectedAmount
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = if (socioToEdit != null) "Editar Socio" else "Registrar Nuevo Socio",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cuota de Colaboración (€):",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (amount in cuotasPredeterminadas) {
                    FilterChip(
                        selected = (currentCuota == amount && customAmountText.isBlank()),
                        onClick = {
                            selectedAmount = amount
                            customAmountText = ""
                        },
                        label = { Text("${amount.toInt()} €") }
                    )
                }
                FilterChip(
                    selected = customAmountText.isNotBlank(),
                    onClick = {
                        if (customAmountText.isBlank()) customAmountText = "50"
                    },
                    label = { Text("Otro") }
                )
            }

            if (customAmountText.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = customAmountText,
                    onValueChange = { customAmountText = it },
                    label = { Text("Importe personalizado (€)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Día de la semana:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            val dias = listOf(1 to "Lun", 2 to "Mar", 3 to "Mié", 4 to "Jue", 5 to "Vie")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for ((id, nombre) in dias) {
                    FilterChip(
                        selected = selectedDiaId == id,
                        onClick = { selectedDiaId = id },
                        label = { Text(nombre) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombreText,
                onValueChange = { nombreText = it },
                label = { Text("Nombre del socio (Opcional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = notasText,
                onValueChange = { notasText = it },
                label = { Text("Notas / Observaciones") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Estado inicial:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isHecho) "CONSEGUIDO" else "PENDIENTE",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isHecho) MaterialTheme.colorScheme.primary else OrangePending
                    )
                }

                Switch(
                    checked = isHecho,
                    onCheckedChange = { isHecho = it }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val baseX2 = CalculoComisiones.calcularBaseX2(currentCuota)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "💡 Generará +${baseX2.toFormattedEuros(2)} de ganancias base (Cuota ${currentCuota.toFormattedEuros(0)} x2)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val socio = socioToEdit?.copy(
                            colaboracion = currentCuota,
                            hecho = isHecho,
                            diaSemanaId = selectedDiaId,
                            nombreSocio = nombreText.trim(),
                            notas = notasText.trim()
                        ) ?: Socio(
                            colaboracion = currentCuota,
                            hecho = isHecho,
                            diaSemanaId = selectedDiaId,
                            semanaKey = semanaKey,
                            nombreSocio = nombreText.trim(),
                            notas = notasText.trim()
                        )
                        onSave(socio)
                        onDismiss()
                    }
                ) {
                    Text("Guardar Socio")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
