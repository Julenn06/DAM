package dam2.reto1.grupo1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit

class MainActivity : BaseActivity() {

    private val registroLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(
                    this,
                    getString(R.string.usuario_registrado_exito),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailEditText = findViewById<EditText>(R.id.editTextText)
        val contrasenaEditText = findViewById<EditText>(R.id.editTextText2)
        val loginButton = findViewById<Button>(R.id.button)
        val registrotxt = findViewById<TextView>(R.id.textView2)
        val recordarCheckBox = findViewById<CheckBox>(R.id.checkBox)

        // Cargar las credenciales guardadas
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("password", null)
        if (savedEmail != null && savedPassword != null) {
            emailEditText.setText(savedEmail)
            contrasenaEditText.setText(savedPassword)
            recordarCheckBox.isChecked = true
        }

        loginButton.setOnClickListener {
            login(emailEditText, contrasenaEditText, recordarCheckBox)
        }

        registrotxt.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            registroLauncher.launch(intent)
        }
    }

    private fun login(
        emailEditText: EditText,
        contrasenaEditText: EditText,
        recordarCheckBox: CheckBox
    ) {
        val email = emailEditText.text.toString().lowercase().trim()
        val contrasena = contrasenaEditText.text.toString().trim()

        if (email.isNotEmpty() && contrasena.isNotEmpty()) {
            Usuario.existeUsuario(email,
                exito = { existe ->
                    if (!existe) {
                        Toast.makeText(
                            baseContext,
                            getString(R.string.usuario_no_existe),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Usuario.login(email, contrasena, { usuario ->
                            GymDaji.usuario = usuario
                            if (recordarCheckBox.isChecked) {
                                sharedPreferences.edit {
                                    putString("email", email)
                                    putString("password", contrasena)
                                }
                            } else {
                                sharedPreferences.edit {
                                    clear()
                                }
                            }
                            Toast.makeText(
                                baseContext,
                                getString(R.string.bienvenido_usuario, usuario.nombre),
                                Toast.LENGTH_SHORT
                            ).show()

                            GymDaji.conseguirdatos(
                                activity = this@MainActivity,
                                usuario = usuario,
                                exito = {
                                    val intent = Intent(this@MainActivity, WorkoutsLista::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            )
                        }, {
                            if (it != null) {
                                Toast.makeText(baseContext, getString(R.string.error_mensaje, it.message), Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(
                                    baseContext,
                                    getString(R.string.contrasena_incorrecta),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    }
                },
                fallo = { exception ->
                    Toast.makeText(
                        baseContext,
                        getString(R.string.error_mensaje, exception.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        } else {
            Toast.makeText(
                this,
                getString(R.string.introducir_credenciales),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun imprimirWorkouts() {

        GymDaji.workouts.forEach {
            println("Workout: ${it.nombre}")
            println("Descripcion: ${it.descripcion}")
            println("Nivel: ${it.nivel}")
            println("Video: ${it.video}")

            it.ejercicios.forEach { ejercicio ->
                println("  Ejercicio: ${ejercicio.nombre}")
                println("    Descripcion: ${ejercicio.descripcion}")
                println("    Tiempo de descanso: ${ejercicio.tiempoDescanso}")
                println("    Series:")
                ejercicio.series.forEach { serie ->
                    println("      Nombre: ${serie.nombre}")
                    println("      Foto: ${serie.foto}")
                    println("      Tiempo: ${serie.tiempo}")
                }


            }
        }


    }
}