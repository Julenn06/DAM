package dam2.reto1.grupo1

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Date

class Registro : BaseActivity() {

    private var fechaNacimientoDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nombreEditText = findViewById<TextInputEditText>(R.id.editTextText3)
        val apellidosEditText = findViewById<TextInputEditText>(R.id.editTextText4)
        val emailEditText = findViewById<TextInputEditText>(R.id.editTextText)
        val contrasenaEditText = findViewById<TextInputEditText>(R.id.editTextText2)
        val fechaNacimientoEditText = findViewById<TextInputEditText>(R.id.editTextDate)
        val registroButton = findViewById<Button>(R.id.button)
        val volverTextView = findViewById<TextView>(R.id.textView3)
        val spinner = findViewById<Spinner>(R.id.spinner)

        fechaNacimientoEditText.setOnClickListener {
            mostrarDatePicker()
        }

        volverTextView.setOnClickListener {
            finish()
        }

        registroButton.setOnClickListener {
            mRegistro(nombreEditText, apellidosEditText, emailEditText, contrasenaEditText, spinner)
        }
    }

    private fun mRegistro(nombreEditText: TextInputEditText, apellidosEditText: TextInputEditText, emailEditText: TextInputEditText, contrasenaEditText: TextInputEditText, spinner: Spinner) {
        val nombre = nombreEditText.text.toString().trim()
        val apellidos = apellidosEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val contrasena = contrasenaEditText.text.toString().trim()
        val rol = spinner.selectedItem.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, getString(R.string.correo_invalido), Toast.LENGTH_SHORT).show()
            return
        }

        if (contrasena.length < 4) {
            Toast.makeText(this, getString(R.string.contrasena_corta), Toast.LENGTH_SHORT).show()
            return
        }

        val fechaNacimiento = fechaNacimientoDate

        if (nombre.isNotEmpty() && apellidos.isNotEmpty() && email.isNotEmpty() && contrasena.isNotEmpty() && fechaNacimiento != null && rol.isNotEmpty()) {
            if (rol != "Cliente" && rol != "Entrenador") {
                Toast.makeText(this, getString(R.string.rol_invalido), Toast.LENGTH_SHORT).show()
                return
            }
            val usuario = Usuario(
                nombre = nombre,
                apellidos = apellidos,
                email = email,
                contrasena = contrasena,
                fec_nac = fechaNacimiento,
                tipo = rol.lowercase()
            )
            usuario.registro({
                setResult(Activity.RESULT_OK)
                finish()
            }, {
                Toast.makeText(this, getString(R.string.error_registro, it.message), Toast.LENGTH_SHORT).show()
            })
        } else {
            Toast.makeText(this, getString(R.string.campos_vacios), Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDateStr = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                findViewById<TextInputEditText>(R.id.editTextDate).setText(selectedDateStr)

                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                fechaNacimientoDate = selectedCalendar.time
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}