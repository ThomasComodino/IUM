package com.example.superspan.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.superspan.MainActivity
import com.example.superspan.AdminActivity // Creeremo questa ora
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. RIPRISTINO DATI SALVATI (Ricordami)
        val pref = getSharedPreferences("SUperSpanPrefs", MODE_PRIVATE)
        val savedEmail = pref.getString("email", "")
        val savedPass = pref.getString("pass", "")

        if (!savedEmail.isNullOrBlank()) {
            binding.tilEmail.editText?.setText(savedEmail)
            binding.tilPassword.editText?.setText(savedPass)
            binding.cbRemember.isChecked = true
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.tilEmail.editText?.text.toString()
            val pass = binding.tilPassword.editText?.text.toString()

            // 2. CONTROLLO CAMPI VUOTI (Tua richiesta di sicurezza)
            if (email.isBlank() || pass.isBlank()) {
                if (email.isBlank()) binding.tilEmail.error = "Campo obbligatorio" else binding.tilEmail.error = null
                if (pass.isBlank()) binding.tilPassword.error = "Campo obbligatorio" else binding.tilPassword.error = null
                Toast.makeText(this, "Riempi tutti i campi!", Toast.LENGTH_SHORT).show()
            } else {
                // Se i campi sono pieni, procediamo con la logica di salvataggio
                val editor = pref.edit()
                if (binding.cbRemember.isChecked) {
                    editor.putString("email", email)
                    editor.putString("pass", pass)
                } else {
                    // Rimuoviamo solo i dati di login, non tutte le preferenze
                    editor.remove("email")
                    editor.remove("pass")
                }
                editor.apply()

                // 3. AUTENTICAZIONE REALE
                val authenticatedUser = FakeRepository.authenticate(email, pass)

                if (authenticatedUser != null) {
                    // Pulizia errori
                    binding.tilEmail.error = null
                    binding.tilPassword.error = null

                    if (authenticatedUser.isAdmin) {
                        Toast.makeText(this, "Accesso Amministratore", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, AdminActivity::class.java))
                    } else {
                        Toast.makeText(this, "Benvenuto, ${authenticatedUser.name}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    finish()
                } else {
                    // Feedback credenziali errate
                    binding.tilEmail.error = "Credenziali non valide"
                    binding.tilPassword.error = "Verifica email e password"
                    Toast.makeText(this, "Email o Password errati!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}