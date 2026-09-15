package com.example.caendario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caendario.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val scope = CoroutineScope(Dispatchers.Main)
    private val RC_SIGN_IN = 9001
    private val coursesAdapter = CoursesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = coursesAdapter

        // Configura OAuth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id)) // <-- MUY IMPORTANTE
            .requestScopes(
                com.google.android.gms.common.api.Scope("https://www.googleapis.com/auth/classroom.courses.readonly")
            )
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Si ya está logueado, no hace falta volver a loguear
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            fetchCourses(account)
        }

        // Botón Login
        binding.btnLogin.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(Exception::class.java)
                if (account != null) {
                    Toast.makeText(this, "Login correcto: ${account.displayName}", Toast.LENGTH_SHORT).show()
                    fetchCourses(account)
                } else {
                    Toast.makeText(this, "Error: cuenta nula", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Login fallido: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fetchCourses(account: GoogleSignInAccount) {
        scope.launch {
            try {
                val token = account.idToken
                if (token == null) {
                    Toast.makeText(this@MainActivity, "Token nulo, no autorizado", Toast.LENGTH_LONG).show()
                    return@launch
                }

                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://classroom.googleapis.com/v1/courses")
                    .addHeader("Authorization", "Bearer $token")
                    .build()

                val response = withContext(Dispatchers.IO) {
                    client.newCall(request).execute()
                }

                if (response.isSuccessful) {
                    val json = response.body?.string()
                    val gson = Gson()
                    val courses = gson.fromJson(json, CoursesResponse::class.java)
                    coursesAdapter.submitList(courses.courses)
                    Toast.makeText(this@MainActivity, "Cursos cargados ✅", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Error ${response.code}: ${response.body?.string()}", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
