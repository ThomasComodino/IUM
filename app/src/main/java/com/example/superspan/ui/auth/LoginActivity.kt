package com.example.superspan.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.superspan.MainActivity
import com.example.superspan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- LOGICA RICORDAMI ---
        val pref = getSharedPreferences("SUperSpanPrefs", MODE_PRIVATE)
        val savedEmail = pref.getString("email", "")
        val savedPass = pref.getString("pass", "") // Aggiungiamo anche la password

        if (!savedEmail.isNullOrBlank()) {
            binding.tilEmail.editText?.setText(savedEmail)
            binding.tilPassword.editText?.setText(savedPass)
            binding.cbRemember.isChecked = true
        }

        // --- BOTTONE ACCEDI (Con Validazione) ---
        binding.btnLogin.setOnClickListener {
            val email = binding.tilEmail.editText?.text.toString()
            val pass = binding.tilPassword.editText?.text.toString()

            // Controllo se i campi sono vuoti
            if (email.isBlank() || pass.isBlank()) {
                if (email.isBlank()) binding.tilEmail.error = "Inserisci l'email" else binding.tilEmail.error = null
                if (pass.isBlank()) binding.tilPassword.error = "Inserisci la password" else binding.tilPassword.error = null
                Toast.makeText(this, "Campi obbligatori mancanti!", Toast.LENGTH_SHORT).show()
            } else {
                // Se tutto è pieno, salviamo (se checked) o cancelliamo (se unchecked)
                val editor = pref.edit()
                if (binding.cbRemember.isChecked) {
                    editor.putString("email", email)
                    editor.putString("pass", pass)
                } else {
                    editor.clear() // Cancella tutto se togli la spunta
                }
                editor.apply()

                // Navigazione
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        // --- LINK REGISTRATI (Ripristinato) ---
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}