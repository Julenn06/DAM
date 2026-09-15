package com.example.repaso01

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class FirestoreManager {

    private val db = FirebaseFirestore.getInstance()

    /**
     * Añadir o actualizar un documento.
     * Si el documento existe lo sobrescribe.
     */
    fun saveDocument(
        collection: String,
        documentId: String,
        data: Any,
        onComplete: (Boolean) -> Unit
    ) {
        db.collection(collection)
            .document(documentId)
            .set(data)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    /**
     * Añadir documento con ID autogenerado por Firestore.
     * Firestore genera automáticamente un ID aleatorio único.
     */
    fun addDocument(
        collection: String,
        data: Any,
        onComplete: (Boolean, String?) -> Unit
    ) {
        db.collection(collection)
            .add(data)
            .addOnSuccessListener { documentReference ->
                // Retorna true y el ID generado
                onComplete(true, documentReference.id)
            }
            .addOnFailureListener {
                // Retorna false y null
                onComplete(false, null)
            }
    }

    /**
     * Leer un documento una sola vez.
     */
    fun getDocument(
        collection: String,
        documentId: String,
        onResult: (Map<String, Any>?) -> Unit
    ) {
        db.collection(collection)
            .document(documentId)
            .get()
            .addOnSuccessListener { snapshot ->
                onResult(snapshot.data)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    /**
     * Leer una colección completa una sola vez.
     * Incluye el ID del documento en el Map con la clave "__id__"
     */
    fun getCollection(
        collection: String,
        onResult: (List<Map<String, Any>>) -> Unit
    ) {
        db.collection(collection)
            .get()
            .addOnSuccessListener { query ->
                val list = query.documents.mapNotNull { document ->
                    document.data?.toMutableMap()?.apply {
                        // Añadir el ID del documento al Map
                        put("__id__", document.id)
                    }
                }
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    /**
     * Escuchar cambios en un documento en tiempo real.
     * Devuelve un ListenerRegistration que puedes quitar en onDestroy().
     */
    fun listenDocument(
        collection: String,
        documentId: String,
        onChanged: (Map<String, Any>?) -> Unit
    ): ListenerRegistration {
        return db.collection(collection)
            .document(documentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onChanged(null)
                    return@addSnapshotListener
                }
                onChanged(snapshot?.data)
            }
    }

    /**
     * Escuchar una colección en tiempo real.
     */
    fun listenCollection(
        collection: String,
        onChanged: (List<Map<String, Any>>) -> Unit
    ): ListenerRegistration {
        return db.collection(collection)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    onChanged(emptyList())
                    return@addSnapshotListener
                }

                val list = snapshots?.documents?.mapNotNull { it.data } ?: emptyList()
                onChanged(list)
            }
    }

    /**
     * Eliminar un documento de Firestore.
     * Esta operación es irreversible - elimina permanentemente el documento.
     * 
     * FIRESTORE DELETE OPERATION:
     * ===========================
     * - Elimina un documento por su ID
     * - No genera error si el documento no existe
     * - No elimina subcolecciones automáticamente
     * - Operación atómica e inmediata
     * 
     * @param collection Nombre de la colección
     * @param documentId ID del documento a eliminar
     * @param onComplete Callback que retorna true si tuvo éxito, false si falló
     */
    fun deleteDocument(
        collection: String,
        documentId: String,
        onComplete: (Boolean) -> Unit
    ) {
        db.collection(collection)
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                // Documento eliminado exitosamente
                onComplete(true)
            }
            .addOnFailureListener {
                // Error al eliminar
                onComplete(false)
            }
    }
}
