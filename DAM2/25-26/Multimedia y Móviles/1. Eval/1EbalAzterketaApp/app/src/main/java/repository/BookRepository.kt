package com.example.a1ebalazterketaapp.repository

import com.example.a1ebalazterketaapp.modelo.Book
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BookRepository {
    private val db = FirebaseFirestore.getInstance()
    private val booksCollection = db.collection("books")
    
    /**
     * Añade un libro a Firestore
     */
    suspend fun addBook(book: Book): Result<String> {
        return try {
            val docRef = booksCollection.add(book).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene todos los libros de Firestore
     */
    suspend fun getAllBooks(): Result<List<Book>> {
        return try {
            val snapshot = booksCollection.get().await()
            val books = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Book::class.java)?.apply {
                    id = doc.id
                }
            }
            Result.success(books)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene un libro por su ID
     */
    suspend fun getBookById(bookId: String): Result<Book> {
        return try {
            val doc = booksCollection.document(bookId).get().await()
            val book = doc.toObject(Book::class.java)?.apply {
                id = doc.id
            }
            if (book != null) {
                Result.success(book)
            } else {
                Result.failure(Exception("Libro no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Actualiza un libro existente
     */
    suspend fun updateBook(bookId: String, book: Book): Result<Unit> {
        return try {
            booksCollection.document(bookId).set(book).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Elimina un libro por su ID
     */
    suspend fun deleteBook(bookId: String): Result<Unit> {
        return try {
            booksCollection.document(bookId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
