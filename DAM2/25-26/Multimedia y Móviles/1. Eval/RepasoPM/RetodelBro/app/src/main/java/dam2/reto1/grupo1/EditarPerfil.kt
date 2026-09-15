package dam2.reto1.grupo1

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Date

class EditarPerfil : BaseActivity() {

    private var fechaNacimientoDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editarperfil)

        val nombreEditText = findViewById<TextInputEditText>(R.id.editTextText3)
        val apellidosEditText = findViewById<TextInputEditText>(R.id.editTextText4)
        val emailEditText = findViewById<TextInputEditText>(R.id.editTextText)
        val contrasenaEditText = findViewById<TextInputEditText>(R.id.editTextText2)
        val fechaNacimientoEditText = findViewById<TextInputEditText>(R.id.editTextDate)
        val registroButton = findViewById<Button>(R.id.button)
        val volverTextView = findViewById<TextView>(R.id.textView3)
        val idiomaSpinner = findViewById<Spinner>(R.id.spinner)
        val temaSpinner = findViewById<Spinner>(R.id.spinner3)

        // Set current language and theme in spinners
        val sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)

        // Language Spinner
        val currentLang = sharedPreferences.getString("language", "es")
        val langPosition = when (currentLang) {
            "es" -> 0
            "eu" -> 1
            "en" -> 2
            else -> 0
        }
        idiomaSpinner.setSelection(langPosition, false)

        idiomaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLangCode = when (position) {
                    0 -> "es"
                    1 -> "eu"
                    2 -> "en"
                    else -> "es"
                }

                val currentSavedLang = sharedPreferences.getString("language", "es")

                if (currentSavedLang != selectedLangCode) {
                    cambiarIdioma(selectedLangCode)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Theme Spinner
        val currentTheme = sharedPreferences.getString("theme", "light")
        val themePosition = when (currentTheme) {
            "light" -> 0
            "dark" -> 1
            else -> 0
        }
        temaSpinner.setSelection(themePosition, false)

        temaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedTheme = when (position) {
                    0 -> "light"
                    1 -> "dark"
                    else -> "light"
                }

                val currentSavedTheme = sharedPreferences.getString("theme", "light")

                if (currentSavedTheme != selectedTheme) {
                    cambiarTema(selectedTheme)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        fechaNacimientoEditText.setOnClickListener {
            mostrarDatePicker()
        }

        volverTextView.setOnClickListener {
            finish()
        }

        // Prefill fields if there is a logged-in user
        val currentUser = GymDaji.usuario
        if (currentUser != null) {
            nombreEditText.setText(currentUser.nombre)
            apellidosEditText.setText(currentUser.apellidos)
            emailEditText.setText(currentUser.email)
            contrasenaEditText.setText(currentUser.contrasena)
            currentUser.fec_nac?.let {
                val cal = Calendar.getInstance().apply { time = it }
                val dateStr = "${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.YEAR)}"
                fechaNacimientoEditText.setText(dateStr)
                fechaNacimientoDate = it
            }
            // Si el usuario tiene idUsuario, lo usaremos internamente en Firestore
            // Cambiar texto del botón a "Guardar" o similar
            registroButton.text = getString(R.string.editarperfil)
        }

        registroButton.setOnClickListener {
            // Si hay usuario actual, editar; si no, registrar nuevo usuario
            if (currentUser != null) {
                mEditarPerfil(nombreEditText, apellidosEditText, emailEditText, contrasenaEditText)
            }
        }
    }

    private fun cambiarIdioma(langCode: String) {
        val sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        sharedPreferences.edit { putString("language", langCode) }
        recreate()
    }

    private fun cambiarTema(theme: String) {
        val sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        sharedPreferences.edit { putString("theme", theme) }
        recreate()
    }



    private fun mEditarPerfil(nombreEditText: TextInputEditText, apellidosEditText: TextInputEditText, emailEditText: TextInputEditText, contrasenaEditText: TextInputEditText) {
        val nombre = nombreEditText.text.toString().trim()
        val apellidos = apellidosEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val contrasena = contrasenaEditText.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, getString(R.string.correo_invalido), Toast.LENGTH_SHORT).show()
            return
        }

        if (contrasena.length < 4) {
            Toast.makeText(this, getString(R.string.contrasena_corta), Toast.LENGTH_SHORT).show()
            return
        }

        val fechaNacimiento = fechaNacimientoDate

        if (nombre.isNotEmpty() && apellidos.isNotEmpty() && email.isNotEmpty() && contrasena.isNotEmpty() && fechaNacimiento != null) {
            val rol = GymDaji.usuario?.tipo ?: "cliente"
            val usuario = Usuario(
                idUsuario = GymDaji.usuario?.idUsuario ?: "",
                nombre = nombre,
                apellidos = apellidos,
                email = email,
                contrasena = contrasena,
                fec_nac = fechaNacimiento,
                tipo = rol.lowercase(),
                nivel = GymDaji.usuario?.nivel ?: 0,
                workouts = GymDaji.usuario?.workouts ?: emptyList()
            )
            usuario.editarPerfil({
                // Actualizar credenciales guardadas si existen (loginPrefs)
                val loginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE)
                loginPrefs.edit {

                        putString("email", usuario.email)
                        putString("password", usuario.contrasena)

                }
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
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
