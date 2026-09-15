package dam2.reto1.grupo1

import java.util.Date

data class Ejercicio(
    val idEjercicio: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val tiempoDescanso: Int = 0,
    val series: List<Serie> = emptyList()
) {
    companion object {
        fun getEjerciciosWorkout(workout: Workout, onResult: (List<Ejercicio>) -> Unit) {
            val instance = GymDaji.firestore
            val coleccionEjercicios = instance.collection("workouts").document(workout.idWorkout).collection("ejercicios")
            coleccionEjercicios.get()
                .addOnSuccessListener { ejercicioDocuments ->
                    val ejercicios = mutableListOf<Ejercicio>()
                    val totalEjercicios = ejercicioDocuments.size()
                    if (totalEjercicios == 0) {
                        onResult(emptyList())
                        return@addOnSuccessListener
                    }

                    for (ejercicioDoc in ejercicioDocuments) {
                        val ejercicio = ejercicioDoc.toObject(Ejercicio::class.java).copy(idEjercicio = ejercicioDoc.id)
                        Serie.getSeriesEjercicio(workout.idWorkout, ejercicio) { series ->
                            ejercicios.add(ejercicio.copy(series = series))
                            if (ejercicios.size == totalEjercicios) {
                                onResult(ejercicios)
                            }
                        }
                    }
                }
        }
    }
}

data class Serie(
    val idSerie: String = "",
    val nombre: String = "",
    val foto: String = "",
    val tiempo: Int = 0
) {
    companion object {
        fun getSeriesEjercicio(workoutId: String, ejercicio: Ejercicio, exito: (List<Serie>) -> Unit) {
            val db = GymDaji.firestore
            val coleccionseries = db.collection("workouts").document(workoutId)
                .collection("ejercicios").document(ejercicio.idEjercicio).collection("series")
            coleccionseries.get()
                .addOnSuccessListener { documentos ->
                    val series = documentos.documents.map { doc ->
                        doc.toObject(Serie::class.java)!!.copy(idSerie = doc.id)
                    }
                    exito(series)
                }
        }
    }
}

data class Usuario(
    val idUsuario: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val contrasena: String = "",
    val fec_nac: Date? = null,
    val nivel: Int = 0,
    val tipo: String = "",
    val workouts: List<UsuWorkout> = emptyList()
) {
    fun registro(
        exito: () -> Unit,
        error: (Exception) -> Unit
    ) {
        val db = GymDaji.firestore
        db.collection("usuarios").document(this.email)
            .set(this)
            .addOnSuccessListener { exito() }
            .addOnFailureListener { e -> error(e) }
    }

    /**
     * Actualiza el perfil del usuario en Firestore.
     * Actualiza por `idUsuario` cuando esté disponible. Si no, busca el documento por email y usa su id.
     * Mantiene compatibilidad con documentos donde el id era el email.
     */
    fun editarPerfil(
        exito: () -> Unit,
        error: (Exception) -> Unit
    ) {
        val db = GymDaji.firestore

        val updateFields = mutableMapOf<String, Any>()
        updateFields["nombre"] = this.nombre
        updateFields["apellidos"] = this.apellidos
        updateFields["email"] = this.email
        updateFields["contrasena"] = this.contrasena
        this.fec_nac?.let { updateFields["fec_nac"] = it }
        // Si tenemos un idUsuario válido, actualizar directamente por id (solo campos permitidos)
        if (this.idUsuario.isNotBlank()) {
            db.collection("usuarios").document(this.idUsuario)
                .update(updateFields)
                .addOnSuccessListener {
                    // Actualizar la referencia local sin tocar nivel ni workouts
                    val current = GymDaji.usuario
                    GymDaji.usuario = if (current != null) {
                        current.copy(
                            nombre = this.nombre,
                            apellidos = this.apellidos,
                            email = this.email,
                            contrasena = this.contrasena,
                            fec_nac = this.fec_nac
                        )
                    } else {
                        // Si no hay usuario en memoria, crear uno con idUsuario y mantener nivel por defecto
                        this.copy(idUsuario = this.idUsuario)
                    }
                    exito()
                }
                .addOnFailureListener { e -> error(e) }
            return
        }

        db.collection("usuarios").whereEqualTo("email", this.email).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val docId = querySnapshot.documents.first().id
                    // actualizamos usando el id encontrado (solo campos permitidos)
                    db.collection("usuarios").document(docId)
                        .update(updateFields)
                        .addOnSuccessListener {
                            // actualizar idUsuario localmente por si hace falta después, sin tocar nivel ni workouts
                            val current = GymDaji.usuario
                            GymDaji.usuario = if (current != null) {
                                current.copy(
                                    idUsuario = docId,
                                    nombre = this.nombre,
                                    apellidos = this.apellidos,
                                    email = this.email,
                                    contrasena = this.contrasena,
                                    fec_nac = this.fec_nac,
                                    tipo = this.tipo
                                )
                            } else {
                                this.copy(idUsuario = docId)
                            }
                            exito()
                        }
                        .addOnFailureListener { e -> error(e) }
                } else {
                    val nivelActual = GymDaji.usuario?.nivel ?: this.nivel
                    val workoutsActual = GymDaji.usuario?.workouts ?: this.workouts
                    val userToSet = this.copy(nivel = nivelActual, workouts = workoutsActual)
                    db.collection("usuarios").document(this.email)
                        .set(userToSet)
                        .addOnSuccessListener {
                            GymDaji.usuario = userToSet
                            exito()
                        }
                        .addOnFailureListener { e -> error(e) }
                }
            }
            .addOnFailureListener { e -> error(e) }
    }

    companion object {
        /**
         * Verifica si existe un usuario con el email proporcionado
         */
        fun existeUsuario(email: String, exito: (Boolean) -> Unit, fallo: (Exception) -> Unit) {
            val db = GymDaji.firestore
            db.collection("usuarios")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    exito(!documents.isEmpty)
                }
                .addOnFailureListener { exception ->
                    fallo(exception)
                }
        }

        fun login(email: String, clave: String, exito: (Usuario) -> Unit, fallo: (Exception?) -> Unit) {
            val db = GymDaji.firestore
            db.collection("usuarios")
                .whereEqualTo("email", email)
                .whereEqualTo("contrasena", clave)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        fallo(null)
                    } else {
                        val doc = documents.first()
                        val user = doc.toObject(Usuario::class.java).copy(idUsuario = doc.id)
                        exito(user)
                     }
                 }
                .addOnFailureListener { exception ->
                    fallo(exception)
                }
        }
    }
}

data class UsuWorkout(
    val idHistorial: String = "",
    val workout: Workout = Workout(),
    val tiempo_total: Int = 0,
    val fecha: Date? = null,
    val ejercicioscompletados: Double = 0.0
){
    companion object{
        fun getHistorialUsuario(
            idUsuario: String,
            exito: (List<UsuWorkout>) -> Unit,
            fallo: (Exception) -> Unit
        ) {
            val db = GymDaji.firestore
            db.collection("usuarios").document(idUsuario)
                .collection("workouts")
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.isEmpty) {
                        exito(emptyList())
                        return@addOnSuccessListener
                    }

                    val historialIncompleto = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(UsuWorkout::class.java)?.copy(idHistorial = doc.id)
                    }

                    val historialCompleto = mutableListOf<UsuWorkout>()
                    val totalWorkouts = historialIncompleto.size

                    if (totalWorkouts == 0) {
                        exito(emptyList())
                        return@addOnSuccessListener
                    }

                    historialIncompleto.forEach { usuWorkout ->
                        val doc = snapshot.documents.first { it.id == usuWorkout.idHistorial }
                        val workoutRef = doc.getDocumentReference("id_workout")
                        if (workoutRef == null) {
                            if (historialCompleto.size + 1 == totalWorkouts) {
                                exito(historialCompleto)
                            }
                            return@forEach
                        }

                        val idWorkout = workoutRef.id
                        Workout.cargarWorkoutPorId(idWorkout,
                            { workoutDesdeColeccion ->
                                // Now we have the main workout data, let'''s fetch its exercises.
                                Ejercicio.getEjerciciosWorkout(workoutDesdeColeccion) { ejercicios ->
                                    val workoutCompleto = workoutDesdeColeccion.copy(ejercicios = ejercicios)
                                    val usuWorkoutCompleto = usuWorkout.copy(workout = workoutCompleto)
                                    historialCompleto.add(usuWorkoutCompleto)

                                    if (historialCompleto.size == totalWorkouts) {
                                        exito(historialCompleto)
                                    }
                                }
                            },
                            { e ->
                                // If loading a workout fails, we call the main failure callback.
                                fallo(e)
                            }
                        )
                    }
                }
                .addOnFailureListener { e -> fallo(e) }
        }
    }
}

data class Workout(
    val idWorkout: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val nivel: Int = 0,
    val video: String = "",
    val ejercicios: List<Ejercicio> = emptyList()
) {
    fun getTiempoPrevistoSegundos(): Int {
        var tiempoPrevisto = 0
        val PREPARACION_POR_SERIE = 5

        ejercicios.forEach { e ->
            e.series.forEach { s ->
                tiempoPrevisto += s.tiempo
                tiempoPrevisto += e.tiempoDescanso
                tiempoPrevisto += PREPARACION_POR_SERIE
            }
        }

        return tiempoPrevisto
    }

    companion object {
        fun getWorkoutsUsuario(
            usuario: Usuario,
            exito: (ArrayList<Workout>) -> Unit,
            fallo: (Exception) -> Unit
        ) {
            val db = GymDaji.firestore
            db.collection("workouts")
                .whereLessThanOrEqualTo("nivel", usuario.nivel)
                .get()
                .addOnSuccessListener { workoutDocumentos ->
                    val workouts = arrayListOf<Workout>()
                    val totalWorkouts = workoutDocumentos.size()
                    if (totalWorkouts == 0) {
                        exito(arrayListOf())
                        return@addOnSuccessListener
                    }

                    for (workoutDoc in workoutDocumentos) {
                        val workout =
                            workoutDoc.toObject(Workout::class.java).copy(idWorkout = workoutDoc.id)
                        Ejercicio.getEjerciciosWorkout(workout) { ejercicios ->
                            workouts.add(workout.copy(ejercicios = ejercicios))
                            if (workouts.size == totalWorkouts) {
                                exito(workouts)
                            }
                        }
                    }
                }
                .addOnFailureListener { e -> fallo(e) }
        }

        /**
         * Crea un nuevo workout, calculando un id numérico secuencial (como en el código previo).
         * Devuelve el Workout creado en `exito` o la excepción en `fallo`.
         */
        fun crearWorkout(
            nombre: String,
            descripcion: String,
            video: String,
            nivel: Int,
            exito: (Workout) -> Unit,
            fallo: (Exception) -> Unit
        ) {
            val db = GymDaji.firestore
            db.collection("workouts").get()
                .addOnSuccessListener { workoutsSnapshot ->
                    val maxId =
                        workoutsSnapshot.documents.mapNotNull { it.id.toIntOrNull() }.maxOrNull()
                            ?: 0
                    val newId = (maxId + 1).toString()
                    val workout = Workout(
                        idWorkout = newId,
                        nombre = nombre,
                        descripcion = descripcion,
                        nivel = nivel,
                        video = video,
                        ejercicios = emptyList()
                    )

                    db.collection("workouts").document(newId)
                        .set(workout)
                        .addOnSuccessListener { exito(workout) }
                        .addOnFailureListener { e -> fallo(e) }
                }
                .addOnFailureListener { e -> fallo(e) }
        }

        /**
         * Borra un workout por su id.
         */
        fun borrarWorkout(idWorkout: String, exito: () -> Unit, fallo: (Exception) -> Unit) {
            val db = GymDaji.firestore
            db.collection("workouts").document(idWorkout)
                .delete()
                .addOnSuccessListener { exito() }
                .addOnFailureListener { e -> fallo(e) }
        }

        /**
         * Carga un workout por su id.
         */
        fun cargarWorkoutPorId(
            idWorkout: String,
            exito: (Workout) -> Unit,
            fallo: (Exception) -> Unit
        ) {
            val db = GymDaji.firestore
            db.collection("workouts").document(idWorkout).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val workoutTemp = document.toObject(Workout::class.java)
                        if (workoutTemp != null) {
                            val workout = workoutTemp.copy(idWorkout = document.id)
                            exito(workout)
                        } else {
                            fallo(Exception("Error al convertir el documento"))
                        }
                    } else {
                        fallo(Exception("Workout no encontrado"))
                    }
                }
                .addOnFailureListener { e -> fallo(e) }
        }

        /**
         * Actualiza un workout por su id.
         */
        fun actualizarWorkout(
            idWorkout: String,
            nombre: String,
            video: String,
            nivel: Int,
            descripcion: String,
            exito: () -> Unit,
            fallo: (Exception) -> Unit
        ) {
            val db = GymDaji.firestore
            val workoutUpdates = mapOf(
                "nombre" to nombre,
                "video" to video,
                "nivel" to nivel,
                "descripcion" to descripcion
            )

            db.collection("workouts").document(idWorkout)
                .update(workoutUpdates)
                .addOnSuccessListener { exito() }
                .addOnFailureListener { e -> fallo(e) }
        }


    }
}
