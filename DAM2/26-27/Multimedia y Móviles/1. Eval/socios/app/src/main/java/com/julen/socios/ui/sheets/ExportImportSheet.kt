package com.julen.socios.ui.sheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class ExportFormat { PDF, CSV, JSON }
enum class ExportScopeType { SEMANA_ACTIVA, HISTORICO_GLOBAL }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportImportSheet(
    semanaKeyActiva: String,
    onDismiss: () -> Unit,
    onExport: (format: ExportFormat, scopeType: ExportScopeType) -> Unit,
    onImportJson: () -> Unit,
    onImportCsv: () -> Unit,
    sheetState: SheetState
) {
    var format by remember { mutableStateOf(ExportFormat.PDF) }
    var scopeType by remember { mutableStateOf(ExportScopeType.SEMANA_ACTIVA) }

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
                text = "📤 Exportar / Importar Copia",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Formato de Exportación:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Row {
                FilterChip(
                    selected = format == ExportFormat.PDF,
                    onClick = { format = ExportFormat.PDF },
                    label = { Text("PDF (Reporte)") }
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                FilterChip(
                    selected = format == ExportFormat.CSV,
                    onClick = { format = ExportFormat.CSV },
                    label = { Text("CSV (Excel)") }
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                FilterChip(
                    selected = format == ExportFormat.JSON,
                    onClick = { format = ExportFormat.JSON },
                    label = { Text("JSON") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Rango a Exportar:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = scopeType == ExportScopeType.SEMANA_ACTIVA,
                    onClick = { scopeType = ExportScopeType.SEMANA_ACTIVA }
                )
                Text("Semana Activa ($semanaKeyActiva)")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = scopeType == ExportScopeType.HISTORICO_GLOBAL,
                    onClick = { scopeType = ExportScopeType.HISTORICO_GLOBAL }
                )
                Text("Histórico Global (Todas las semanas)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onExport(format, scopeType)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generar y Exportar Archivo")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Restaurar Copia de Seguridad:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = {
                        onImportJson()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Importar JSON")
                }
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                OutlinedButton(
                    onClick = {
                        onImportCsv()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Importar CSV")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
