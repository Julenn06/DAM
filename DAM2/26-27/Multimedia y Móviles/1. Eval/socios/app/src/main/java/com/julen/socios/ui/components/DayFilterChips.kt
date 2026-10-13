package com.julen.socios.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julen.socios.model.Socio

@Composable
fun DayFilterChips(
    selectedDiaId: Int?,
    todosSocios: List<Socio>,
    onSelectDia: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp)
    ) {
        val totalCount = todosSocios.size
        FilterChip(
            selected = selectedDiaId == null,
            onClick = { onSelectDia(null) },
            label = { Text("Todos ($totalCount)") }
        )

        val dias = listOf(
            1 to "Lun",
            2 to "Mar",
            3 to "Mié",
            4 to "Jue",
            5 to "Vie"
        )

        for ((id, nombre) in dias) {
            Spacer(modifier = Modifier.width(6.dp))
            val count = todosSocios.count { it.diaSemanaId == id }
            FilterChip(
                selected = selectedDiaId == id,
                onClick = { onSelectDia(id) },
                label = { Text("$nombre ($count)") }
            )
        }
    }
}
