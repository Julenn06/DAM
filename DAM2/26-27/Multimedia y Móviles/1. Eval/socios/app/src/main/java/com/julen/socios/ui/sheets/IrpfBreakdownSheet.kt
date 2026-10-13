package com.julen.socios.ui.sheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.julen.socios.util.CalculoComisiones
import com.julen.socios.util.toFormattedEuros

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IrpfBreakdownSheet(
    rangoTexto: String,
    resumen: CalculoComisiones.ResumenCalculo?,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
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
                text = "🧾 Desglose Fiscal IRPF (21,48%)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(rangoTexto, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(16.dp))

            resumen?.let { res ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Base Colaboración (Cuotas x2):", modifier = Modifier.weight(1f))
                    Text(res.gananciasBaseX2.toFormattedEuros(2), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Bonus Semanal Acumulado:", modifier = Modifier.weight(1f))
                    Text("+${res.bonusSemanal.toFormattedEuros(2)}", fontWeight = FontWeight.Bold)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("TOTAL BRUTO:", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text(res.totalBruto.toFormattedEuros(2), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Retención IRPF (21,48%):", modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.error)
                    Text("-${res.retencionIrpf.toFormattedEuros(2)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("TOTAL NETO ESTIMADO:", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(res.totalNeto.toFormattedEuros(2), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
