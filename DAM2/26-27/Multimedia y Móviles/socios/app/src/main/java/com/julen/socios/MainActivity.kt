package com.julen.socios

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.julen.socios.data.BonoRegaloRepository
import com.julen.socios.data.SocioRepository
import com.julen.socios.databinding.ActivityMainBinding
import com.julen.socios.model.DiaSemana
import com.julen.socios.model.Socio
import com.julen.socios.util.CalculoComisiones
import com.julen.socios.util.DateUtils
import com.julen.socios.util.ExportUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: SocioRepository
    private lateinit var bonoRegaloRepository: BonoRegaloRepository
    private lateinit var adapter: SocioAdapter

    private var weekOffset = 0
    private var selectedDiaFiltroId: Int? = null // null = Todos, 1=Lunes..5=Viernes

    // --- ACTIVATION / EXPORT & IMPORT LAUNCHERS ---
    private var pendingExportType: String? = null
    private var pendingExportSocios: List<Socio> = emptyList()
    private var pendingExportTitle: String = ""

    private val createDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("*/*")
    ) { uri: Uri? ->
        if (uri != null && pendingExportType != null) {
            try {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    when (pendingExportType) {
                        "pdf" -> {
                            val semanaKey = DateUtils.getSemanaKey(weekOffset)
                            val bonoInfo =
                                if (pendingExportTitle != "Histórico Global") bonoRegaloRepository.getBonoRegalo(
                                    semanaKey
                                ) else null
                            ExportUtils.exportToPdf(
                                pendingExportSocios,
                                pendingExportTitle,
                                outputStream,
                                bonoInfo
                            )
                        }

                        "csv" -> ExportUtils.exportToCsv(pendingExportSocios, outputStream)
                        "json" -> ExportUtils.exportToJson(pendingExportSocios, outputStream)
                    }
                }
                Snackbar.make(
                    binding.root,
                    "¡Archivo guardado correctamente!",
                    Snackbar.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                Snackbar.make(
                    binding.root,
                    "Error al guardar el archivo: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private val importJsonLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val sociosImportados = ExportUtils.importFromJson(inputStream)
                    if (sociosImportados.isNotEmpty()) {
                        val count = repository.importSocios(sociosImportados)
                        Snackbar.make(
                            binding.root,
                            "✓ Se han importado $count socios correctamente",
                            Snackbar.LENGTH_LONG
                        ).show()
                        refreshUi()
                    } else {
                        Snackbar.make(
                            binding.root,
                            "El archivo JSON no contiene socios válidos",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Snackbar.make(
                    binding.root,
                    "Error al importar JSON: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private val importCsvLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val sociosImportados = ExportUtils.importFromCsv(inputStream)
                    if (sociosImportados.isNotEmpty()) {
                        val count = repository.importSocios(sociosImportados)
                        Snackbar.make(
                            binding.root,
                            "✓ Se han importado $count socios correctamente",
                            Snackbar.LENGTH_LONG
                        ).show()
                        refreshUi()
                    } else {
                        Snackbar.make(
                            binding.root,
                            "El archivo CSV no contiene socios válidos",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Snackbar.make(
                    binding.root,
                    "Error al importar CSV: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)

        repository = SocioRepository(this)
        bonoRegaloRepository = BonoRegaloRepository(this)

        setupRecyclerView()
        setupWeekNavigation()
        setupDayFilterChips()
        setupFab()
        setupHeaderClickListeners()

        refreshUi()
    }

    private fun setupRecyclerView() {
        adapter = SocioAdapter(
            onToggleHecho = { socio ->
                val nuevoEstado = repository.toggleSocioHecho(socio.id)
                val msg =
                    if (nuevoEstado) "✓ Socio marcado como HECHO" else "⏳ Socio marcado como NO HECHO"
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                refreshUi()
            },
            onEdit = { socio ->
                openAddEditDialog(socio)
            },
            onDelete = { socio ->
                confirmDeleteSocio(socio)
            }
        )
        binding.rvSocios.layoutManager = LinearLayoutManager(this)
        binding.rvSocios.adapter = adapter
    }

    private fun setupWeekNavigation() {
        binding.btnSemanaAnterior.setOnClickListener {
            weekOffset--
            refreshUi()
        }

        binding.btnSemanaSiguiente.setOnClickListener {
            weekOffset++
            refreshUi()
        }

        binding.btnHoy.setOnClickListener {
            weekOffset = 0
            refreshUi()
        }
    }

    private fun setupDayFilterChips() {
        binding.chipGroupDiasFiltro.setOnCheckedStateChangeListener { _, checkedIds ->
            selectedDiaFiltroId = when {
                checkedIds.contains(R.id.chipFiltroLun) -> 1
                checkedIds.contains(R.id.chipFiltroMar) -> 2
                checkedIds.contains(R.id.chipFiltroMie) -> 3
                checkedIds.contains(R.id.chipFiltroJue) -> 4
                checkedIds.contains(R.id.chipFiltroVie) -> 5
                else -> null
            }
            refreshUi()
        }
    }

    private fun setupFab() {
        binding.fabAddSocio.setOnClickListener {
            openAddEditDialog(null)
        }
        binding.btnAddSocioEmpty.setOnClickListener {
            openAddEditDialog(null)
        }
    }

    private fun setupHeaderClickListeners() {
        binding.btnVerDesglose.setOnClickListener {
            mostrarDesgloseIrpf()
        }
        binding.containerBonusHeader.setOnClickListener {
            mostrarDialogoBonoRegalo()
        }
    }

    private fun openAddEditDialog(socioToEdit: Socio?) {
        val semanaKey = DateUtils.getSemanaKey(weekOffset)
        val defaultDia = selectedDiaFiltroId ?: DateUtils.getDiaSemanaActualId()

        val dialog = AddSocioBottomSheetDialog(
            semanaKey = semanaKey,
            defaultDiaId = defaultDia,
            socioToEdit = socioToEdit,
            onSave = { socio ->
                if (socioToEdit == null) {
                    repository.addSocio(socio)
                    Snackbar.make(
                        binding.root,
                        "¡Socio registrado con éxito!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    repository.updateSocio(socio)
                    Snackbar.make(binding.root, "Socio actualizado", Snackbar.LENGTH_SHORT).show()
                }
                refreshUi()
            }
        )
        dialog.show(supportFragmentManager, "AddSocioDialog")
    }

    private fun confirmDeleteSocio(socio: Socio) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Socio")
            .setMessage("¿Estás seguro de que deseas eliminar este registro de ${socio.colaboracion.toInt()}€?")
            .setPositiveButton("Eliminar") { _, _ ->
                repository.deleteSocio(socio.id)
                Snackbar.make(binding.root, "Socio eliminado", Snackbar.LENGTH_SHORT).show()
                refreshUi()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDesgloseIrpf() {
        val semanaKey = DateUtils.getSemanaKey(weekOffset)
        val sociosSemana = repository.getSociosPorSemana(semanaKey)
        val bonoInfo = bonoRegaloRepository.getBonoRegalo(semanaKey)
        val resumen = CalculoComisiones.calcularResumenSemana(sociosSemana, bonoInfo)
        val rangoTexto = DateUtils.getRangoSemanaTexto(weekOffset)

        val dialog = IrpfBreakdownBottomSheetDialog(rangoTexto, resumen)
        dialog.show(supportFragmentManager, "IrpfBreakdownDialog")
    }

    private fun mostrarHistoricoGlobal() {
        val todosLosSocios = repository.getAllSocios()
        val bonosMap = bonoRegaloRepository.getAllBonosRegalo()
        val resumenGlobal =
            CalculoComisiones.calcularResumenHistoricoGlobal(todosLosSocios, bonosMap)

        val dialog = HistoricoGlobalBottomSheetDialog(
            resumenGlobal = resumenGlobal,
            onSelectSemana = { semanaKey ->
                weekOffset = DateUtils.getWeekOffsetFromKey(semanaKey)
                refreshUi()
            }
        )
        dialog.show(supportFragmentManager, "HistoricoGlobalDialog")
    }

    private fun mostrarDialogoBonoRegalo() {
        val semanaKey = DateUtils.getSemanaKey(weekOffset)
        val rangoTexto = DateUtils.getRangoSemanaTexto(weekOffset)
        val currentBono = bonoRegaloRepository.getBonoRegalo(semanaKey)

        val dialog = BonoRegaloBottomSheetDialog(
            semanaTexto = rangoTexto,
            currentBono = currentBono,
            onSave = { nuevoBono ->
                bonoRegaloRepository.saveBonoRegalo(nuevoBono)
                Snackbar.make(binding.root, "¡Bonus actualizado!", Snackbar.LENGTH_SHORT).show()
                refreshUi()
            },
            onDelete = {
                bonoRegaloRepository.deleteBonoRegalo(semanaKey)
                Snackbar.make(
                    binding.root,
                    "Bonus restablecido a automático",
                    Snackbar.LENGTH_SHORT
                ).show()
                refreshUi()
            }
        )
        dialog.show(supportFragmentManager, "BonoRegaloDialog")
    }

    private fun mostrarDialogoExportImport() {
        val semanaKeyActiva = DateUtils.getSemanaKey(weekOffset)
        val todosLosSocios = repository.getAllSocios()
        val bonosMap = bonoRegaloRepository.getAllBonosRegalo()
        val todasLasSemanasKeys =
            (todosLosSocios.map { it.semanaKey } + bonosMap.keys).distinct().sortedDescending()

        val dialog = ExportImportBottomSheetDialog(
            semanaKeyActiva = semanaKeyActiva,
            todasLasSemanasKeys = todasLasSemanasKeys,
            onExportPdf = { scope ->
                ejecutarExportacion("pdf", scope)
            },
            onExportCsv = { scope ->
                ejecutarExportacion("csv", scope)
            },
            onExportJson = { scope ->
                ejecutarExportacion("json", scope)
            },
            onImportJson = {
                importJsonLauncher.launch("application/json")
            },
            onImportCsv = {
                importCsvLauncher.launch("*/*")
            }
        )
        dialog.show(supportFragmentManager, "ExportImportDialog")
    }

    private fun ejecutarExportacion(tipo: String, scope: ExportScope) {
        val semanaKeyActiva = DateUtils.getSemanaKey(weekOffset)
        val allSocios = repository.getAllSocios()

        val (socios, titulo, bonoInfoForPdf) = when (scope) {
            is ExportScope.SemanaActiva -> {
                val sociosSemana = repository.getSociosPorSemana(semanaKeyActiva)
                val rango = DateUtils.getRangoSemanaTexto(weekOffset)
                val bono = bonoRegaloRepository.getBonoRegalo(semanaKeyActiva)
                Triple(sociosSemana, rango, bono)
            }

            is ExportScope.SemanasEspecificas -> {
                val keys = scope.semanaKeys
                val sociosFiltrados = allSocios.filter { it.semanaKey in keys }
                val tituloTexto = if (keys.size == 1) {
                    DateUtils.getRangoSemanaTextoFromKey(keys.first())
                } else {
                    "${keys.size} semanas seleccionadas"
                }
                val bono =
                    if (keys.size == 1) bonoRegaloRepository.getBonoRegalo(keys.first()) else null
                Triple(sociosFiltrados, tituloTexto, bono)
            }

            is ExportScope.HistoricoGlobal -> {
                Triple(allSocios, "Histórico Global", null)
            }
        }

        if (socios.isEmpty()) {
            Snackbar.make(
                binding.root,
                "No hay socios para exportar en el rango seleccionado",
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        val opciones = arrayOf("Compartir con otra app", "Guardar en el dispositivo")
        AlertDialog.Builder(this)
            .setTitle("Exportar ${tipo.uppercase(Locale.getDefault())}")
            .setItems(opciones) { _, which ->
                if (which == 0) {
                    val file = when (tipo) {
                        "pdf" -> ExportUtils.exportToPdfFile(this, socios, titulo, bonoInfoForPdf)
                        "csv" -> ExportUtils.exportToCsvFile(this, socios)
                        else -> ExportUtils.exportToJsonFile(this, socios)
                    }
                    if (file != null) {
                        val mime = when (tipo) {
                            "pdf" -> "application/pdf"
                            "csv" -> "text/csv"
                            else -> "application/json"
                        }
                        ExportUtils.shareFile(this, file, mime, "Compartir reporte $tipo")
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Error al generar archivo temporal",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                } else {
                    pendingExportType = tipo
                    pendingExportSocios = socios
                    pendingExportTitle = titulo

                    val timestamp =
                        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                    val defaultFileName = "socios_${semanaKeyActiva}_$timestamp.$tipo"
                    createDocumentLauncher.launch(defaultFileName)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun refreshUi() {
        val semanaKey = DateUtils.getSemanaKey(weekOffset)
        val todosSociosSemana = repository.getSociosPorSemana(semanaKey)
        val bonoInfoSemana = bonoRegaloRepository.getBonoRegalo(semanaKey)

        // Rango de fechas
        binding.tvRangoSemana.text = DateUtils.getRangoSemanaTexto(weekOffset)
        binding.tvSemanaSubtitulo.text =
            if (weekOffset == 0) "Semana actual (Lunes a Viernes)" else "Semana $semanaKey"
        binding.btnHoy.visibility = if (weekOffset != 0) View.VISIBLE else View.GONE

        // Cálculos generales de la semana
        val resumenSemana =
            CalculoComisiones.calcularResumenSemana(todosSociosSemana, bonoInfoSemana)

        // Actualizar Card Financiera de la semana
        binding.tvNetoHeader.text =
            String.format(Locale.getDefault(), "%.2f €", resumenSemana.totalNeto)
        binding.tvBaseHeader.text =
            String.format(Locale.getDefault(), "+%.0f €", resumenSemana.gananciasBaseX2)
        binding.tvBonusHeader.text =
            String.format(Locale.getDefault(), "+%.0f €", resumenSemana.bonusSemanal)
        binding.tvIrpfHeader.text =
            String.format(Locale.getDefault(), "-%.2f €", resumenSemana.retencionIrpf)

        binding.tvBonusHeaderLabel.text =
            if (resumenSemana.esBonoRegaloAplicado) "Bonus (Manual) ✏️" else "Bonus Socios ✏️"

        // Actualizar Card de Bonus
        val bonusActual = resumenSemana.bonusSemanal.toInt()
        val hechosCount = resumenSemana.totalSociosHechos
        val metaSocios = resumenSemana.siguienteMetaSocios
        val metaBonus = resumenSemana.siguienteMetaBonus.toInt()

        binding.tvBonusTitle.text = "🏆 Bonus Nivel: +$bonusActual € ($hechosCount socios hechos)"
        binding.tvBonusProgressCount.text = "$hechosCount / $metaSocios socios"

        val progressPercent =
            ((hechosCount.toFloat() / metaSocios.toFloat()) * 100).toInt().coerceAtMost(100)
        binding.progressBonus.progress = progressPercent

        if (resumenSemana.sociosFaltantesParaSiguienteMeta > 0) {
            val faltan = resumenSemana.sociosFaltantesParaSiguienteMeta
            binding.tvBonusSiguienteMeta.text =
                "¡Haz $faltan ${if (faltan == 1) "socio más" else "socios más"} para alcanzar el Bonus de +$metaBonus €!"
        } else {
            binding.tvBonusSiguienteMeta.text =
                "¡Enhorabuena! Has alcanzado el nivel de bonus máximo para este tramo."
        }

        // Actualizar contadores de los Chips de Filtro por Día
        val countLun = todosSociosSemana.count { it.diaSemanaId == 1 }
        val countMar = todosSociosSemana.count { it.diaSemanaId == 2 }
        val countMie = todosSociosSemana.count { it.diaSemanaId == 3 }
        val countJue = todosSociosSemana.count { it.diaSemanaId == 4 }
        val countVie = todosSociosSemana.count { it.diaSemanaId == 5 }
        val countTodos = todosSociosSemana.size

        binding.chipFiltroTodos.text = "Todos ($countTodos)"
        binding.chipFiltroLun.text = "Lun ($countLun)"
        binding.chipFiltroMar.text = "Mar ($countMar)"
        binding.chipFiltroMie.text = "Mié ($countMie)"
        binding.chipFiltroJue.text = "Jue ($countJue)"
        binding.chipFiltroVie.text = "Vie ($countVie)"

        // Filtrar lista por el día seleccionado
        val listaFiltrada = if (selectedDiaFiltroId != null) {
            todosSociosSemana.filter { it.diaSemanaId == selectedDiaFiltroId }
        } else {
            todosSociosSemana
        }

        // Actualizar Lista y Empty State
        adapter.submitList(listaFiltrada)

        if (listaFiltrada.isEmpty()) {
            binding.containerEmptyState.visibility = View.VISIBLE
            binding.rvSocios.visibility = View.GONE
        } else {
            binding.containerEmptyState.visibility = View.GONE
            binding.rvSocios.visibility = View.VISIBLE
        }

        // Título de la lista
        val diaNombre = selectedDiaFiltroId?.let { DiaSemana.fromId(it).nombreCompleto }
        binding.tvListaTitulo.text =
            if (diaNombre != null) "Socios del $diaNombre" else "Socios de la semana"

        val hechosFiltrados = listaFiltrada.count { it.hecho }
        binding.tvListaResumenDia.text = "$hechosFiltrados hechos / ${listaFiltrada.size} reg."
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ver_historico -> {
                mostrarHistoricoGlobal()
                true
            }

            R.id.action_ver_desglose -> {
                mostrarDesgloseIrpf()
                true
            }

            R.id.action_export_import -> {
                mostrarDialogoExportImport()
                true
            }

            R.id.action_borrar_semana -> {
                confirmarBorrarSemana()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmarBorrarSemana() {
        val semanaKey = DateUtils.getSemanaKey(weekOffset)
        AlertDialog.Builder(this)
            .setTitle("Limpiar semana")
            .setMessage("¿Deseas borrar todos los socios de la semana $semanaKey?")
            .setPositiveButton("Borrar") { _, _ ->
                repository.clearSemana(semanaKey)
                bonoRegaloRepository.deleteBonoRegalo(semanaKey)
                Snackbar.make(binding.root, "Semana vaciada", Snackbar.LENGTH_SHORT).show()
                refreshUi()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
