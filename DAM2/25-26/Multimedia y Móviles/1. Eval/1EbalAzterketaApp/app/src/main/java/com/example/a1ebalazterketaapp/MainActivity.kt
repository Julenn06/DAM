package com.example.a1ebalazterketaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a1ebalazterketaapp.auth.AuthManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var etUser: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private val authManager = AuthManager()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (authManager.isLoggedIn) {
            navigateToBookList()
            return
        }
        
        initViews()
        setupListeners()
    }
    
    private fun initViews() {
        etUser = findViewById(R.id.etUser)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnVerLista)
    }
    
    private fun setupListeners() {
        btnLogin.setOnClickListener {
            performLogin()
        }
    }
    
    private fun performLogin() {
        val email = etUser.text.toString().trim()
        val password = etPassword.text.toString().trim()
        
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Datu guztiak bete", Toast.LENGTH_SHORT).show()
            return
        }
        
        btnLogin.isEnabled = false
        
        lifecycleScope.launch {
            authManager.login(email, password)
                .onSuccess { user ->
                    Toast.makeText(this@MainActivity, "Ongi Etorri ${user.email}", Toast.LENGTH_SHORT).show()
                    navigateToBookList()
                }
                .onFailure { e ->
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    btnLogin.isEnabled = true
                }
        }
    }
    
    private fun navigateToBookList() {
        val intent = Intent(this, BookListActivity::class.java)
        startActivity(intent)
        finish()
    }
}