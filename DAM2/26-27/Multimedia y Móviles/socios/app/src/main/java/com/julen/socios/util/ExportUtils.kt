package com.julen.socios.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.julen.socios.model.BonoRegaloInfo
import com.julen.socios.model.DiaSemana
import com.julen.socios.model.Socio
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object ExportUtils {

    // ==========================================
    // EXPORT & IMPORT JSON
    // ==========================================

    fun exportToJson(socios: List<Socio>, outputStream: OutputStream) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonString = gson.toJson(socios)
        outputStream.write(jsonString.toByteArray(Charsets.UTF_8))
    }

    fun exportToJsonFile(context: Context, socios: List<Socio>): File? {
        return try {
            val fileName = "socios_export_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.json"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            file.parentFile?.mkdirs()
            FileOutputStream(file).use { out ->
                exportToJson(socios, out)
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun importFromJson(inputStream: InputStream): List<Socio> {
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val gson = GsonBuilder().create()
        val listType = object : TypeToken<List<Socio>>() {}.type
        return gson.fromJson(jsonString, listType) ?: emptyList()
    }

    // ==========================================
    // EXPORT & IMPORT CSV
    // ==========================================

    fun exportToCsv(socios: List<Socio>, outputStream: OutputStream) {
        outputStream.bufferedWriter(Charsets.UTF_8).use { out ->
            out.write("ID,Colaboracion,Hecho,DiaSemanaId,SemanaKey,NombreSocio,Notas,Timestamp\n")
            for (s in socios) {
                val nombreEscaped = escapeCsvField(s.nombreSocio)
                val notasEscaped = escapeCsvField(s.notas)
                out.write("${s.id},${s.colaboracion},${s.hecho},${s.diaSemanaId},${s.semanaKey},$nombreEscaped,$notasEscaped,${s.timestamp}\n")
            }
        }
    }

    fun exportToCsvFile(context: Context, socios: List<Socio>): File? {
        return try {
            val fileName = "socios_export_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.csv"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            file.parentFile?.mkdirs()
            FileOutputStream(file).use { out ->
                exportToCsv(socios, out)
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun importFromCsv(inputStream: InputStream): List<Socio> {
        val socios = mutableListOf<Socio>()
        val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))
        val lines = reader.readLines()

        if (lines.isEmpty()) return emptyList()

        // Skip header if present
        val startIndex = if (lines.first().startsWith("ID", ignoreCase = true)) 1 else 0

        for (i in startIndex until lines.size) {
            val line = lines[i].trim()
            if (line.isBlank()) continue

            val tokens = parseCsvLine(line)
            if (tokens.size >= 5) {
                try {
                    val id = if (tokens.getOrNull(0).isNull0rBlank()) UUID.randomUUID().toString() else tokens[0]
                    val colaboracion = tokens.getOrNull(1)?.toDoubleOrNull() ?: 0.0
                    val hecho = tokens.getOrNull(2)?.toBooleanStrictOrNull() ?: (tokens.getOrNull(2) == "1")
                    val diaSemanaId = tokens.getOrNull(3)?.toIntOrNull() ?: 1
                    val semanaKey = tokens.getOrNull(4) ?: DateUtils.getSemanaKey(0)
                    val nombreSocio = tokens.getOrNull(5) ?: ""
                    val notas = tokens.getOrNull(6) ?: ""
                    val timestamp = tokens.getOrNull(7)?.toLongOrNull() ?: System.currentTimeMillis()

                    socios.add(
                        Socio(
                            id = id,
                            colaboracion = colaboracion,
                            hecho = hecho,
                            diaSemanaId = diaSemanaId,
                            semanaKey = semanaKey,
                            nombreSocio = nombreSocio,
                            notas = notas,
                            timestamp = timestamp
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return socios
    }

    private fun escapeCsvField(field: String): String {
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            val escaped = field.replace("\"", "\"\"")
            return "\"$escaped\""
        }
        return field
    }

    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val sb = StringBuilder()
        var inQuotes = false

        var i = 0
        while (i < line.length) {
            val c = line[i]
            if (c == '"') {
                if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                    sb.append('"')
                    i++
                } else {
                    inQuotes = !inQuotes
                }
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString().trim())
                sb.clear()
            } else {
                sb.append(c)
            }
            i++
        }
        result.add(sb.toString().trim())
        return result
    }

    private fun String?.isNull0rBlank(): Boolean {
        return this == null || this.trim().isEmpty()
    }

    // ==========================================
    // EXPORT PDF WITH PROFESSIONAL DESIGN
    // ==========================================

    fun exportToPdf(
        context: Context,
        socios: List<Socio>,
        titulo: String,
        outputStream: OutputStream,
        bonoRegalo: BonoRegaloInfo? = null
    ) {
        val pdfDocument = PdfDocument()
        val pageWidth = 595 // A4 width in points
        val pageHeight = 842 // A4 height in points

        var pageNum = 1
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas: Canvas = page.canvas

        val paint = Paint()
        val resumen = CalculoComisiones.calcularResumenSemana(socios, bonoRegalo)
        val fechaReporte = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        // --- DIBUJAR ENCABEZADO PRINCIPAL ---
        val headerPaint = Paint().apply {
            color = Color.parseColor("#0D47A1") // Dark Blue
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, pageWidth.toFloat(), 85f, headerPaint)

        // Texto Encabezado
        paint.color = Color.WHITE
        paint.textSize = 20f
        paint.isFakeBoldText = true
        canvas.drawText("GESTIÓN DE SOCIOS - REPORTE", 30f, 40f, paint)

        paint.textSize = 12f
        paint.isFakeBoldText = false
        paint.color = Color.parseColor("#E3F2FD")
        canvas.drawText("Periodo: $titulo  •  Generado: $fechaReporte", 30f, 65f, paint)

        // --- TARJETAS KPI RESUMEN ---
        var y = 105f

        val kpiBgPaint = Paint().apply {
            color = Color.parseColor("#F4F6F9")
            style = Paint.Style.FILL
        }
        val kpiBorderPaint = Paint().apply {
            color = Color.parseColor("#E0E0E0")
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }

        val cardWidth = 125f
        val cardHeight = 55f
        val startX = 30f
        val gap = 11f

        data class KpiData(val label: String, val value: String, val colorHex: String)
        val kpis = listOf(
            KpiData("Socios Hechos", "${resumen.totalSociosHechos} / ${resumen.totalSociosRegistrados}", "#1565C0"),
            KpiData("Base (Cuotas x2)", String.format(Locale.getDefault(), "%.2f €", resumen.gananciasBaseX2), "#2E7D32"),
            KpiData("Bonus Semanal", String.format(Locale.getDefault(), "%.2f €", resumen.bonusSemanal), "#F57F17"),
            KpiData("Ganancia Neta", String.format(Locale.getDefault(), "%.2f €", resumen.totalNeto), "#0D47A1")
        )

        for (idx in kpis.indices) {
            val left = startX + idx * (cardWidth + gap)
            val rect = RectF(left, y, left + cardWidth, y + cardHeight)
            canvas.drawRoundRect(rect, 8f, 8f, kpiBgPaint)
            canvas.drawRoundRect(rect, 8f, 8f, kpiBorderPaint)

            paint.color = Color.parseColor("#616161")
            paint.textSize = 10f
            paint.isFakeBoldText = false
            canvas.drawText(kpis[idx].label, left + 10f, y + 20f, paint)

            paint.color = Color.parseColor(kpis[idx].colorHex)
            paint.textSize = 13f
            paint.isFakeBoldText = true
            canvas.drawText(kpis[idx].value, left + 10f, y + 42f, paint)
        }

        y += cardHeight + 25f

        // --- FUNCIÓN CABECERA DE TABLA ---
        fun drawTableHeader(c: Canvas, currentY: Float): Float {
            val tableHeaderPaint = Paint().apply {
                color = Color.parseColor("#1565C0")
                style = Paint.Style.FILL
            }
            c.drawRoundRect(RectF(30f, currentY, pageWidth - 30f, currentY + 26f), 4f, 4f, tableHeaderPaint)

            paint.color = Color.WHITE
            paint.textSize = 11f
            paint.isFakeBoldText = true

            c.drawText("Nombre / Socio", 40f, currentY + 17f, paint)
            c.drawText("Cuota", 220f, currentY + 17f, paint)
            c.drawText("Día", 285f, currentY + 17f, paint)
            c.drawText("Estado", 350f, currentY + 17f, paint)
            c.drawText("Semana", 420f, currentY + 17f, paint)
            c.drawText("Notas", 480f, currentY + 17f, paint)

            return currentY + 30f
        }

        y = drawTableHeader(canvas, y)

        // --- FILAS DE LA TABLA ---
        val rowBgEven = Paint().apply { color = Color.parseColor("#FFFFFF"); style = Paint.Style.FILL }
        val rowBgOdd = Paint().apply { color = Color.parseColor("#F8FAFC"); style = Paint.Style.FILL }
        val rowBorder = Paint().apply { color = Color.parseColor("#E2E8F0"); style = Paint.Style.STROKE; strokeWidth = 0.8f }

        val rowHeight = 22f

        for (index in socios.indices) {
            // Verificar fin de página
            if (y > pageHeight - 60f) {
                // Pie de página antes de cerrar
                drawFooter(canvas, pageNum, pageWidth, pageHeight)
                pdfDocument.finishPage(page)

                pageNum++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas

                y = 40f
                y = drawTableHeader(canvas, y)
            }

            val socio = socios[index]
            val bgPaint = if (index % 2 == 0) rowBgEven else rowBgOdd

            canvas.drawRect(30f, y, pageWidth - 30f, y + rowHeight, bgPaint)
            canvas.drawRect(30f, y, pageWidth - 30f, y + rowHeight, rowBorder)

            paint.textSize = 10f
            paint.isFakeBoldText = false
            paint.color = Color.parseColor("#1E293B")

            val nombreText = socio.nombreSocio.ifBlank { "Socio ${socio.colaboracion.toInt()}€" }
            val nombreCorto = if (nombreText.length > 25) nombreText.substring(0, 23) + "..." else nombreText
            canvas.drawText(nombreCorto, 40f, y + 15f, paint)

            paint.isFakeBoldText = true
            canvas.drawText(String.format(Locale.getDefault(), "%.0f €", socio.colaboracion), 220f, y + 15f, paint)
            paint.isFakeBoldText = false

            val diaNombre = DiaSemana.fromId(socio.diaSemanaId).nombreCorto
            canvas.drawText(diaNombre, 285f, y + 15f, paint)

            if (socio.hecho) {
                paint.color = Color.parseColor("#2E7D32")
                canvas.drawText("✓ Hecho", 350f, y + 15f, paint)
            } else {
                paint.color = Color.parseColor("#C62828")
                canvas.drawText("⏳ Pendiente", 350f, y + 15f, paint)
            }

            paint.color = Color.parseColor("#64748B")
            canvas.drawText(socio.semanaKey, 420f, y + 15f, paint)

            val notasText = if (socio.notas.length > 15) socio.notas.substring(0, 13) + "..." else socio.notas
            canvas.drawText(notasText, 480f, y + 15f, paint)

            y += rowHeight
        }

        // Pie de página final
        drawFooter(canvas, pageNum, pageWidth, pageHeight)
        pdfDocument.finishPage(page)

        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
    }

    private fun drawFooter(canvas: Canvas, pageNum: Int, pageWidth: Int, pageHeight: Int) {
        val paint = Paint().apply {
            color = Color.parseColor("#94A3B8")
            textSize = 9f
            isAntiAlias = true
        }
        canvas.drawLine(30f, pageHeight - 35f, pageWidth - 30f, pageHeight - 35f, paint)
        canvas.drawText("Gestión de Socios • Documento Oficial", 30f, pageHeight - 20f, paint)
        canvas.drawText("Página $pageNum", pageWidth - 70f, pageHeight - 20f, paint)
    }

    fun exportToPdfFile(
        context: Context,
        socios: List<Socio>,
        titulo: String,
        bonoRegalo: BonoRegaloInfo? = null
    ): File? {
        return try {
            val fileName = "socios_report_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.pdf"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            file.parentFile?.mkdirs()
            FileOutputStream(file).use { out ->
                exportToPdf(context, socios, titulo, out, bonoRegalo)
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // ==========================================
    // COMPARTIR ARCHIVOS CON OTRAS APPS (INTENT)
    // ==========================================

    fun shareFile(context: Context, file: File, mimeType: String, title: String) {
        try {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, title))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
