package com.julen.socios.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.julen.socios.MainViewModel
import com.julen.socios.model.Socio
import com.julen.socios.ui.components.DayFilterChips
import com.julen.socios.ui.components.HeaderKpiSection
import com.julen.socios.ui.components.SocioItemCard
import com.julen.socios.ui.sheets.AddSocioSheet
import com.julen.socios.ui.sheets.BonoRegaloSheet
import com.julen.socios.ui.sheets.ExportFormat
import com.julen.socios.ui.sheets.ExportImportSheet
import com.julen.socios.ui.sheets.ExportScopeType
import com.julen.socios.ui.sheets.HistoricoGlobalSheet
import com.julen.socios.ui.sheets.IrpfBreakdownSheet
import com.julen.socios.util.CalculoComisiones
import com.julen.socios.util.ExportUtils
import kotlinx.coroutines.launch

sealed class ActiveSheet {
    object None : ActiveSheet()
    data class AddEditSocio(val socio: Socio? = null) : ActiveSheet()
    object BonoRegalo : ActiveSheet()
    object ExportImport : ActiveSheet()
    object HistoricoGlobal : ActiveSheet()
    object IrpfBreakdown : ActiveSheet()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var activeSheet by remember { mutableStateOf<ActiveSheet>(ActiveSheet.None) }
    var historicoData by remember { mutableStateOf<CalculoComisiones.ResumenHistoricoGlobal?>(null) }

    var pendingExportFormat by remember { mutableStateOf(ExportFormat.PDF) }
    var pendingExportScopeType by remember { mutableStateOf(ExportScopeType.SEMANA_ACTIVA) }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*")
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                try {
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        val sociosToExport = if (pendingExportScopeType == ExportScopeType.HISTORICO_GLOBAL) {
                            viewModel.getAllSocios()
                        } else {
                            uiState.todosSociosSemana
                        }
                        val bonosToExport = if (pendingExportScopeType == ExportScopeType.HISTORICO_GLOBAL) {
                            viewModel.getAllBonosRegalo().values.toList()
                        } else {
                            listOfNotNull(uiState.bonoInfoSemana)
                        }

                        when (pendingExportFormat) {
                            ExportFormat.PDF -> {
                                val bonoInfo = if (pendingExportScopeType == ExportScopeType.SEMANA_ACTIVA) {
                                    uiState.bonoInfoSemana
                                } else null
                                val title = if (pendingExportScopeType == ExportScopeType.HISTORICO_GLOBAL) {
                                    "Histórico Global"
                                } else uiState.rangoTexto

                                ExportUtils.exportToPdf(sociosToExport, title, outputStream, bonoInfo)
                            }
                            ExportFormat.CSV -> ExportUtils.exportToCsv(sociosToExport, bonosToExport, outputStream)
                            ExportFormat.JSON -> ExportUtils.exportToJson(sociosToExport, bonosToExport, outputStream)
                        }
                    }
                    snackbarHostState.showSnackbar("✓ Archivo guardado correctamente")
                } catch (e: Exception) {
                    snackbarHostState.showSnackbar("Error al guardar archivo: ${e.message}")
                }
            }
        }
    }

    val importJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                try {
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        val result = ExportUtils.importFromJson(inputStream)
                        if (result.socios.isNotEmpty() || result.bonosRegalo.isNotEmpty()) {
                            viewModel.importBackup(result.socios, result.bonosRegalo)
                        } else {
                            snackbarHostState.showSnackbar("El archivo JSON no contiene datos válidos")
                        }
                    }
                } catch (e: Exception) {
                    snackbarHostState.showSnackbar("Error al importar JSON: ${e.message}")
                }
            }
        }
    }

    val importCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                try {
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        val result = ExportUtils.importFromCsv(inputStream)
                        if (result.socios.isNotEmpty() || result.bonosRegalo.isNotEmpty()) {
                            viewModel.importBackup(result.socios, result.bonosRegalo)
                        } else {
                            snackbarHostState.showSnackbar("El archivo CSV no contiene datos válidos")
                        }
                    }
                } catch (e: Exception) {
                    snackbarHostState.showSnackbar("Error al importar CSV: ${e.message}")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.messageEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gestión de Socios",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                val todosSocios = viewModel.getAllSocios()
                                val bonosMap = viewModel.getAllBonosRegalo()
                                historicoData = CalculoComisiones.calcularResumenHistoricoGlobal(todosSocios, bonosMap)
                                activeSheet = ActiveSheet.HistoricoGlobal
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Histórico Global"
                        )
                    }

                    IconButton(
                        onClick = { activeSheet = ActiveSheet.ExportImport }
                    ) {
                        Icon(
                            imageVector = Icons.Default.IosShare,
                            contentDescription = "Exportar / Importar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { activeSheet = ActiveSheet.AddEditSocio(null) },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nuevo Socio") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            item {
                HeaderKpiSection(
                    rangoTexto = uiState.rangoTexto,
                    semanaSubtitulo = if (uiState.weekOffset == 0) "Semana actual (Lunes a Viernes)" else "Semana ${uiState.semanaKey}",
                    weekOffset = uiState.weekOffset,
                    resumen = uiState.resumenSemana,
                    bonoInfo = uiState.bonoInfoSemana,
                    onPrevWeek = { viewModel.changeWeekOffset(-1) },
                    onNextWeek = { viewModel.changeWeekOffset(1) },
                    onCurrentWeek = { viewModel.setWeekOffset(0) },
                    onOpenBonusSheet = { activeSheet = ActiveSheet.BonoRegalo },
                    onOpenIrpfSheet = { activeSheet = ActiveSheet.IrpfBreakdown }
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                DayFilterChips(
                    selectedDiaId = uiState.selectedDiaFiltroId,
                    todosSocios = uiState.todosSociosSemana,
                    onSelectDia = { viewModel.setSelectedDiaFiltro(it) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Socios (${uiState.sociosFiltrados.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            if (uiState.sociosFiltrados.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay socios registrados para este filtro.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(
                    items = uiState.sociosFiltrados,
                    key = { it.id }
                ) { socio ->
                    SocioItemCard(
                        socio = socio,
                        onToggleHecho = { viewModel.toggleSocioHecho(it.id) },
                        onEdit = { activeSheet = ActiveSheet.AddEditSocio(it) },
                        onDelete = { viewModel.deleteSocio(it.id) },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }

    when (val sheet = activeSheet) {
        is ActiveSheet.AddEditSocio -> {
            AddSocioSheet(
                semanaKey = uiState.semanaKey,
                defaultDiaId = uiState.selectedDiaFiltroId ?: 1,
                socioToEdit = sheet.socio,
                onDismiss = { activeSheet = ActiveSheet.None },
                onSave = { socio ->
                    if (sheet.socio != null) viewModel.updateSocio(socio) else viewModel.addSocio(socio)
                },
                sheetState = sheetState
            )
        }
        is ActiveSheet.BonoRegalo -> {
            BonoRegaloSheet(
                semanaKey = uiState.semanaKey,
                bonoInfo = uiState.bonoInfoSemana,
                onDismiss = { activeSheet = ActiveSheet.None },
                onSaveBono = { viewModel.saveBonoRegalo(it) },
                onDeleteBono = { viewModel.deleteBonoRegalo(it) },
                sheetState = sheetState
            )
        }
        is ActiveSheet.ExportImport -> {
            ExportImportSheet(
                semanaKeyActiva = uiState.semanaKey,
                onDismiss = { activeSheet = ActiveSheet.None },
                onExport = { format, scopeType ->
                    pendingExportFormat = format
                    pendingExportScopeType = scopeType
                    val ext = when (format) {
                        ExportFormat.PDF -> "pdf"
                        ExportFormat.CSV -> "csv"
                        ExportFormat.JSON -> "json"
                    }
                    createDocumentLauncher.launch("socios_export_${uiState.semanaKey}.$ext")
                },
                onImportJson = { importJsonLauncher.launch("application/json") },
                onImportCsv = { importCsvLauncher.launch("*/*") },
                sheetState = sheetState
            )
        }
        is ActiveSheet.HistoricoGlobal -> {
            HistoricoGlobalSheet(
                historico = historicoData,
                onDismiss = { activeSheet = ActiveSheet.None },
                sheetState = sheetState
            )
        }
        is ActiveSheet.IrpfBreakdown -> {
            IrpfBreakdownSheet(
                rangoTexto = uiState.rangoTexto,
                resumen = uiState.resumenSemana,
                onDismiss = { activeSheet = ActiveSheet.None },
                sheetState = sheetState
            )
        }
        ActiveSheet.None -> {}
    }
}
