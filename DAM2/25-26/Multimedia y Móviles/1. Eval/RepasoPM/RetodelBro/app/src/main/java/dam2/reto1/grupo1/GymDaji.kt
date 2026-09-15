package dam2.reto1.grupo1

import com.google.firebase.firestore.FirebaseFirestore
import android.app.Activity
import android.widget.Toast
import kotlin.getValue

object GymDaji {
    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    var workouts: ArrayList<Workout> = arrayListOf()
    var usuario: Usuario? = null

    // Carga los workouts del usuario y su historial y en caso de error muestra Toasts internos.
    // No realiza navegación; delega al llamador mediante el callback de éxito (sin parámetros).
    fun conseguirdatos(
        activity: Activity,
        usuario: Usuario,
        exito: () -> Unit
    ) {
        Workout.getWorkoutsUsuario(
            usuario,
            exito = { listaDeWorkouts ->
                workouts = ArrayList(listaDeWorkouts)

                UsuWorkout.getHistorialUsuario(
                    usuario.idUsuario,
                    exito = { historial ->
                        val usuarioConWorkouts = usuario.copy(workouts = historial)
                        this.usuario = usuarioConWorkouts
                        exito()
                    },
                    fallo = { exception ->
                        Toast.makeText(
                            activity.applicationContext,
                            "Error al cargar el historial: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            },
            fallo = { exception ->
                Toast.makeText(
                    activity.applicationContext,
                    "Error al cargar los workouts: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }
}
