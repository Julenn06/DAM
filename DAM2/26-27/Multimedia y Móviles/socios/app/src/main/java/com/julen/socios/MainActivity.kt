package com.julen.socios

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.julen.socios.data.SocioRepository
import com.julen.socios.databinding.ActivityMainBinding
import com.julen.socios.model.DiaSemana
import com.julen.socios.model.Socio
import com.julen.socios.util.CalculoComisiones
import com.julen.socios.util.DateUtils
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: SocioRepository
    private lateinit var adapter: SocioAdapter

    private var weekOffset = 0
    private var selectedDiaFiltroId: Int? = null // null = Todos, 1=Lunes..5=Viernes

    override fun onCreate(savedInstanceState: Bundle?) {
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

        // Cargar datos de prueba si es la primera vez que se abre la app
        checkAndSeedSampleData()

        setupRecyclerView()
        setupWeekNavigation()
        setupDayFilterChips()
        setupFab()
        setupHeaderClickListeners()

        refreshUi()
    }

    private fun checkAndSeedSampleData() {
        if (repository.getAllSocios().isEmpty()) {
            val currentSemanaKey = DateUtils.getSemanaKey(0)
            val sampleSocios = listOf(
                Socio(colaboracion = 20.0, hecho = true, diaSemanaId = 1, semanaKey = currentSemanaKey, nombreSocio = "Socio Lunes (Ejemplo)", notas = "Punto presencial"),
                Socio(colaboracion = 30.0, hecho = true, diaSemanaId = 1, semanaKey = currentSemanaKey, nombreSocio = "Socio Lunes Premium", notas = "Cuota mensual 30€"),
                Socio(colaboracion = 15.0, hecho = true, diaSemanaId = 2, semanaKey = currentSemanaKey, nombreSocio = "Socio Martes", notas = ""),
                Socio(colaboracion = 25.0, hecho = true, diaSemanaId = 3, semanaKey = currentSemanaKey, nombreSocio = "Socio Miércoles", notas = "Recomendado"),
                Socio(colaboracion = 20.0, hecho = true, diaSemanaId = 4, semanaKey = currentSemanaKey, nombreSocio = "Socio Jueves", notas = ""),
                Socio(colaboracion = 18.0, hecho = true, diaSemanaId = 5, semanaKey = currentSemanaKey, nombreSocio = "Socio Viernes", notas = ""),
                Socio(colaboracion = 12.0, hecho = false, diaSemanaId = 5, semanaKey = currentSemanaKey, nombreSocio = "Socio Pendiente Viernes", notas = "Volver a llamar")
            )
            sampleSocios.forEach { repository.addSocio(it) }
        }
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
        val resumen = CalculoComisiones.calcularResumenSemana(sociosSemana)
        val rangoTexto = DateUtils.getRangoSemanaTexto(weekOffset)

        val dialog = IrpfBreakdownBottomSheetDialog(rangoTexto, resumen)
        dialog.show(supportFragmentManager, "IrpfBreakdownDialog")
    }

    private fun mostrarHistoricoGlobal() {
        val todosLosSocios = repository.getAllSocios()
        val resumenGlobal = CalculoComisiones.calcularResumenHistoricoGlobal(todosLosSocios)

        val dialog = HistoricoGlobalBottomSheetDialog(
            resumenGlobal = resumenGlobal,
            onSelectSemana = { semanaKey ->
                weekOffset = DateUtils.getWeekOffsetFromKey(semanaKey)
                refreshUi()
            }
        )
        dialog.show(supportFragmentManager, "HistoricoGlobalDialog")
    }

    private fun refreshUi() {
        val semanaKey = DateUtils.getSemanaKey(weekOffset)
        val todosSociosSemana = repository.getSociosPorSemana(semanaKey)

        // Rango de fechas
        binding.tvRangoSemana.text = DateUtils.getRangoSemanaTexto(weekOffset)
        binding.tvSemanaSubtitulo.text = if (weekOffset == 0) "Semana actual (Lunes a Viernes)" else "Semana $semanaKey"
        binding.btnHoy.visibility = if (weekOffset != 0) View.VISIBLE else View.GONE

        // Cálculos generales de la semana
        val resumenSemana = CalculoComisiones.calcularResumenSemana(todosSociosSemana)

        // Actualizar Card Financiera de la semana
        binding.tvNetoHeader.text = String.format(Locale.getDefault(), "%.2f €", resumenSemana.totalNeto)
        binding.tvBaseHeader.text = String.format(Locale.getDefault(), "+%.0f €", resumenSemana.gananciasBaseX2)
        binding.tvBonusHeader.text = String.format(Locale.getDefault(), "+%.0f €", resumenSemana.bonusSemanal)
        binding.tvIrpfHeader.text = String.format(Locale.getDefault(), "-%.2f €", resumenSemana.retencionIrpf)

        // Actualizar Card de Bonus
        val bonusActual = resumenSemana.bonusSemanal.toInt()
        val hechosCount = resumenSemana.totalSociosHechos
        val metaSocios = resumenSemana.siguienteMetaSocios
        val metaBonus = resumenSemana.siguienteMetaBonus.toInt()

        binding.tvBonusTitle.text = "🏆 Bonus Nivel: +$bonusActual € ($hechosCount socios hechos)"
        binding.tvBonusProgressCount.text = "$hechosCount / $metaSocios socios"

        val progressPercent = ((hechosCount.toFloat() / metaSocios.toFloat()) * 100).toInt().coerceAtMost(100)
        binding.progressBonus.progress = progressPercent

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
                Snackbar.make(binding.root, "Semana vaciada", Snackbar.LENGTH_SHORT).show()
                refreshUi()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
