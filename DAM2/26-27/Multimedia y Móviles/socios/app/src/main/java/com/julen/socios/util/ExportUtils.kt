package com.julen.socios.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.julen.socios.model.Socio
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExportUtils {

    fun exportToCsv(context: Context, socios: List<Socio>): File? {
        try {
            val fileName = "socios_export_${
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            }.csv"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            file.parentFile?.mkdirs()

            file.printWriter().use { out ->
                out.println("ID,Colaboración,Hecho,DíaSemanaId,SemanaKey,Nombre,Notas,Timestamp")
                for (s in socios) {
                    out.println("${s.id},${s.colaboracion},${s.hecho},${s.diaSemanaId},${s.semanaKey},\"${s.nombreSocio}\",\"${s.notas}\",${s.timestamp}")
                }
            }
            Toast.makeText(context, "Exportado a CSV: ${file.name}", Toast.LENGTH_LONG).show()
            return file
        } catch (e: Exception) {
            Toast.makeText(context, "Error exportando CSV: ${e.message}", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    fun exportToJson(context: Context, socios: List<Socio>): File? {
        try {
            val fileName = "socios_export_${
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            }.json"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            file.parentFile?.mkdirs()

            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonString = gson.toJson(socios)

            file.writeText(jsonString)
            Toast.makeText(context, "Exportado a JSON: ${file.name}", Toast.LENGTH_LONG).show()
            return file
        } catch (e: Exception) {
            Toast.makeText(context, "Error exportando JSON: ${e.message}", Toast.LENGTH_SHORT)
                .show()
            return null
        }
    }

    fun exportToPdf(context: Context, socios: List<Socio>, titulo: String): File? {
        try {
            val fileName = "socios_report_${
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            }.pdf"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            file.parentFile?.mkdirs()

            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
            val page = pdfDocument.startPage(pageInfo)
            val canvas: Canvas = page.canvas
            val paint = Paint()

            paint.textSize = 18f
            paint.isFakeBoldText = true
            canvas.drawText("Informe de Socios - $titulo", 40f, 50f, paint)

            paint.textSize = 12f
            paint.isFakeBoldText = false
            var y = 90f

            canvas.drawText("Total de socios registrados: ${socios.size}", 40f, y, paint)
            y += 25f
            val hechos = socios.count { it.hecho }
            canvas.drawText("Socios hechos: $hechos", 40f, y, paint)
            y += 35f

            paint.isFakeBoldText = true
            canvas.drawText("Nombre / Cuota", 40f, y, paint)
            canvas.drawText("Día", 250f, y, paint)
            canvas.drawText("Estado", 350f, y, paint)
            canvas.drawText("Semana", 450f, y, paint)
            y += 10f

            canvas.drawLine(40f, y, 555f, y, paint)
            y += 20f

            paint.isFakeBoldText = false
            for (s in socios) {
                if (y > 780f) {
                    break
                }
                val nombre =
                    s.nombreSocio.ifBlank { "Socio ${s.colaboracion.toInt()}€" }
                canvas.drawText("$nombre (${s.colaboracion.toInt()}€)", 40f, y, paint)
                canvas.drawText("Día ${s.diaSemanaId}", 250f, y, paint)
                canvas.drawText(if (s.hecho) "Hecho" else "Pendiente", 350f, y, paint)
                canvas.drawText(s.semanaKey, 450f, y, paint)
                y += 22f
            }

            pdfDocument.finishPage(page)

            FileOutputStream(file).use { fos ->
                pdfDocument.writeTo(fos)
            }
            pdfDocument.close()

            Toast.makeText(context, "Exportado a PDF: ${file.name}", Toast.LENGTH_LONG).show()
            return file
        } catch (e: Exception) {
            Toast.makeText(context, "Error exportando PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            return null
        }
    }
}
