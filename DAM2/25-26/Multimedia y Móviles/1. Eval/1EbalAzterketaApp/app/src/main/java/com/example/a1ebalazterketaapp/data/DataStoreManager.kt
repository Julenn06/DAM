package com.example.a1ebalazterketaapp

import com.example.a1ebalazterketaapp.modelo.Book
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DataStoreManager {
    private val db = FirebaseFirestore.getInstance()
    private val booksCollection = db.collection("books")

    suspend fun addBook(book: Book): Result<String> {
        return try {
            val docRef = booksCollection.add(book).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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

    suspend fun getBookById(bookId: String): Result<Book> {
        return try {
            val doc = booksCollection.document(bookId).get().await()
            val book = doc.toObject(Book::class.java)?.apply {
                id = doc.id
            }
            if (book != null) {
                Result.success(book)
            } else {
                Result.failure(Exception("Liburua ez da aurkitu"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBook(bookId: String, book: Book): Result<Unit> {
        return try {
            booksCollection.document(bookId).set(book).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteBook(bookId: String): Result<Unit> {
        return try {
            booksCollection.document(bookId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
