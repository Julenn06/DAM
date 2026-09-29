package com.julen.socios

import android.net.Uri
import android.os.Bundle
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.julen.socios.data.BonoRegaloRepository
import com.julen.socios.data.SocioRepository
import com.julen.socios.databinding.ActivityMainBinding
import com.julen.socios.model.DiaSemana
import com.julen.socios.model.Socio
import com.julen.socios.util.AnimationExtensions
import com.julen.socios.util.CalculoComisiones
import com.julen.socios.util.DateUtils
import com.julen.socios.util.ExportUtils
import com.julen.socios.util.HapticUtils
import com.julen.socios.util.NumberAnimators
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: SocioRepository
    private lateinit var bonoRegaloRepository: BonoRegaloRepository
    private lateinit var adapter: SocioAdapter

    private lateinit var gestureDetector: GestureDetector

    private var weekOffset = 0
    private var selectedDiaFiltroId: Int? = null // null = Todos, 1=Lunes..5=Viernes
    private var previousBonusAmount: Int = -1

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
                            val bonoInfo = if (pendingExportTitle != "Histórico Global") bonoRegaloRepository.getBonoRegalo(semanaKey) else null
                            ExportUtils.exportToPdf(pendingExportSocios, pendingExportTitle, outputStream, bonoInfo)
                        }
                        "csv" -> ExportUtils.exportToCsv(pendingExportSocios, outputStream)
                        "json" -> ExportUtils.exportToJson(pendingExportSocios, outputStream)
                    }
                }
                Snackbar.make(binding.root, "¡Archivo guardado correctamente!", Snackbar.LENGTH_LONG).show()
            } catch (e: Exception) {
                Snackbar.make(binding.root, "Error al guardar el archivo: ${e.message}", Snackbar.LENGTH_LONG).show()
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
                        Snackbar.make(binding.root, "✓ Se han importado $count socios correctamente", Snackbar.LENGTH_LONG).show()
                        refreshUi()
                    } else {
                        Snackbar.make(binding.root, "El archivo JSON no contiene socios válidos", Snackbar.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Snackbar.make(binding.root, "Error al importar JSON: ${e.message}", Snackbar.LENGTH_LONG).show()
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
                        Snackbar.make(binding.root, "✓ Se han importado $count socios correctamente", Snackbar.LENGTH_LONG).show()
                        refreshUi()
                    } else {
                        Snackbar.make(binding.root, "El archivo CSV no contiene socios válidos", Snackbar.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Snackbar.make(binding.root, "Error al importar CSV: ${e.message}", Snackbar.LENGTH_LONG).show()
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
        setupGestureDetector()

        // Micro-animaciones táctiles en elementos principales
        AnimationExtensions.setupPressScaleAnimation(binding.fabAddSocio)
        AnimationExtensions.setupPressScaleAnimation(binding.containerBonusHeader)
        AnimationExtensions.setupPressScaleAnimation(binding.btnVerDesglose)

        refreshUi()
    }

    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            private val swipeThreshold = 80
            private val swipeVelocityThreshold = 80

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null) return false
                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y

                // Asegurar que el gesto es un deslizamiento horizontal claro y no scroll vertical
                if (abs(diffX) > abs(diffY) * 1.3f) {
                    if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                        if (diffX < 0) {
                            // Deslizar izquierda -> Ir a la semana siguiente
                            HapticUtils.performClick(binding.btnSemanaSiguiente)
                            resetDayFilterToTodos()
                            weekOffset++
                            refreshUi()
                        } else {
                            // Deslizar derecha -> Ir a la semana anterior
                            HapticUtils.performClick(binding.btnSemanaAnterior)
                            resetDayFilterToTodos()
                            weekOffset--
                            refreshUi()
                        }
                        return true
                    }
                }
                return false
            }
        })
    }

    private var isTouchStartedOnDayChips = false

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            isTouchStartedOnDayChips = isTouchInsideView(binding.scrollDiasFiltro, ev)
        }

        if (!isTouchStartedOnDayChips) {
            gestureDetector.onTouchEvent(ev)
        }

        if (ev.actionMasked == MotionEvent.ACTION_UP || ev.actionMasked == MotionEvent.ACTION_CANCEL) {
            isTouchStartedOnDayChips = false
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun isTouchInsideView(view: View, ev: MotionEvent): Boolean {
        if (view.visibility != View.VISIBLE) return false
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]
        val width = view.width
        val height = view.height

        return ev.rawX >= x && ev.rawX <= (x + width) && ev.rawY >= y && ev.rawY <= (y + height)
    }

    private fun resetDayFilterToTodos() {
        selectedDiaFiltroId = null
        binding.chipFiltroTodos.isChecked = true
    }

    private fun setupRecyclerView() {
        adapter = SocioAdapter(
            onToggleHecho = { socio ->
                val nuevoEstado = repository.toggleSocioHecho(socio.id)
                val msg = if (nuevoEstado) "✓ Socio marcado como HECHO" else "⏳ Socio marcado como NO HECHO"
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

        // Ocultar / Encojer el botón flotante al hacer scroll hacia abajo para no tapar contenido
        binding.rvSocios.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 8 && binding.fabAddSocio.isExtended) {
                    binding.fabAddSocio.shrink()
                } else if (dy < -8 && !binding.fabAddSocio.isExtended) {
                    binding.fabAddSocio.extend()
                }
            }
        })
    }

    private fun setupWeekNavigation() {
        binding.btnSemanaAnterior.setOnClickListener {
            resetDayFilterToTodos()
            weekOffset--
            refreshUi()
        }

        binding.btnSemanaSiguiente.setOnClickListener {
            resetDayFilterToTodos()
            weekOffset++
            refreshUi()
        }

        val onHoyClick = View.OnClickListener {
            resetDayFilterToTodos()
            weekOffset = 0
            refreshUi()
        }

        binding.btnHoyLeft.setOnClickListener(onHoyClick)
        binding.btnHoyRight.setOnClickListener(onHoyClick)
    }

    private fun setupDayFilterChips() {
        binding.chipGroupDiasFiltro.setOnCheckedStateChangeListener { chipGroup, checkedIds ->
            HapticUtils.performClick(chipGroup)
            val checkedChipId = checkedIds.firstOrNull()
            if (checkedChipId != null) {
                val chip = chipGroup.findViewById<View>(checkedChipId)
                chip?.animate()
                    ?.scaleX(1.08f)
                    ?.scaleY(1.08f)
                    ?.setDuration(100)
                    ?.withEndAction {
                        chip.animate()
                            ?.scaleX(1.0f)
                            ?.scaleY(1.0f)
                            ?.setDuration(100)
                            ?.start()
                    }
                    ?.start()
            }
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
                    Snackbar.make(binding.root, "¡Socio registrado con éxito!", Snackbar.LENGTH_SHORT).show()
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
        val resumenGlobal = CalculoComisiones.calcularResumenHistoricoGlobal(todosLosSocios, bonosMap)

        val dialog = HistoricoGlobalBottomSheetDialog(
            resumenGlobal = resumenGlobal,
            onSelectSemana = { semanaKey ->
                resetDayFilterToTodos()
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
                Snackbar.make(binding.root, "Bonus restablecido a automático", Snackbar.LENGTH_SHORT).show()
                refreshUi()
            }
        )
        dialog.show(supportFragmentManager, "BonoRegaloDialog")
    }

    private fun mostrarDialogoExportImport() {
        val semanaKeyActiva = DateUtils.getSemanaKey(weekOffset)
        val todosLosSocios = repository.getAllSocios()
        val bonosMap = bonoRegaloRepository.getAllBonosRegalo()
        val todasLasSemanasKeys = (todosLosSocios.map { it.semanaKey } + bonosMap.keys).distinct().sortedDescending()

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
                val bono = if (keys.size == 1) bonoRegaloRepository.getBonoRegalo(keys.first()) else null
                Triple(sociosFiltrados, tituloTexto, bono)
            }
            is ExportScope.HistoricoGlobal -> {
                Triple(allSocios, "Histórico Global", null)
            }
        }

        if (socios.isEmpty()) {
            Snackbar.make(binding.root, "No hay socios para exportar en el rango seleccionado", Snackbar.LENGTH_LONG).show()
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
                        Snackbar.make(binding.root, "Error al generar archivo temporal", Snackbar.LENGTH_LONG).show()
                    }
                } else {
                    pendingExportType = tipo
                    pendingExportSocios = socios
                    pendingExportTitle = titulo

                    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
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
        binding.tvSemanaSubtitulo.text = if (weekOffset == 0) "Semana actual (Lunes a Viernes)" else "Semana $semanaKey"

        // Posicionamiento inteligente del botón Hoy (Izquierda para semanas futuras, Derecha para semanas pasadas)
        if (weekOffset < 0) {
            binding.btnHoyLeft.visibility = View.GONE
            binding.btnHoyRight.visibility = View.VISIBLE
        } else if (weekOffset > 0) {
            binding.btnHoyLeft.visibility = View.VISIBLE
            binding.btnHoyRight.visibility = View.GONE
        } else {
            binding.btnHoyLeft.visibility = View.GONE
            binding.btnHoyRight.visibility = View.GONE
        }

        // Cálculos generales de la semana
        val resumenSemana = CalculoComisiones.calcularResumenSemana(todosSociosSemana, bonoInfoSemana)

        // Actualizar Card Financiera con Animaciones de Conteo de Números (Count-up Animators)
        NumberAnimators.animateCurrency(binding.tvNetoHeader, resumenSemana.totalNeto, prefix = "")
        NumberAnimators.animateCurrency(binding.tvBaseHeader, resumenSemana.gananciasBaseX2, prefix = "+", decimals = 0)
        NumberAnimators.animateCurrency(binding.tvBonusHeader, resumenSemana.bonusSemanal, prefix = "+", decimals = 0)
        NumberAnimators.animateCurrency(binding.tvIrpfHeader, resumenSemana.retencionIrpf, prefix = "-")

        binding.tvBonusHeaderLabel.text = if (resumenSemana.esBonoRegaloAplicado) "Bonus (Manual) ✏️" else "Bonus Socios ✏️"

        // Actualizar Card de Bonus
        val bonusActual = resumenSemana.bonusSemanal.toInt()
        val hechosCount = resumenSemana.totalSociosHechos
        val metaSocios = resumenSemana.siguienteMetaSocios
        val metaBonus = resumenSemana.siguienteMetaBonus.toInt()

        binding.tvBonusTitle.text = "🏆 Bonus Nivel: +$bonusActual € ($hechosCount socios hechos)"
        binding.tvBonusProgressCount.text = "$hechosCount / $metaSocios socios"

        // Animación de pulso elástico cuando se incrementa el bonus
        if (previousBonusAmount in 0 until bonusActual) {
            binding.tvBonusTitle.animate()
                .scaleX(1.12f)
                .scaleY(1.12f)
                .setDuration(200)
                .setInterpolator(OvershootInterpolator(2.5f))
                .withEndAction {
                    binding.tvBonusTitle.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(150)
                        .start()
                }
                .start()
        }
        previousBonusAmount = bonusActual

        val progressPercent = ((hechosCount.toFloat() / metaSocios.toFloat()) * 100).toInt().coerceAtMost(100)
        AnimationExtensions.animateProgressSmooth(binding.progressBonus, progressPercent)

        if (resumenSemana.sociosFaltantesParaSiguienteMeta > 0) {
            val faltan = resumenSemana.sociosFaltantesParaSiguienteMeta
            binding.tvBonusSiguienteMeta.text = "¡Haz $faltan ${if (faltan == 1) "socio más" else "socios más"} para alcanzar el Bonus de +$metaBonus €!"
        } else {
            binding.tvBonusSiguienteMeta.text = "¡Enhorabuena! Has alcanzado el nivel de bonus máximo para este tramo."
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

        // Actualizar Lista y Empty State de forma nativa e instantánea
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
        binding.tvListaTitulo.text = if (diaNombre != null) "Socios del $diaNombre" else "Socios de la semana"

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
