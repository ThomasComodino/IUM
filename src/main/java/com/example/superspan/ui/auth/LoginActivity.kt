package com.example.superspan.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.superspan.AdminActivity
import com.example.superspan.MainActivity
import com.example.superspan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences("SUperSpanPrefs", MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            // Pulizia input: trim e lowercase come richiesto
            val email = binding.tilEmail.editText?.text.toString().trim().lowercase()
            val pass = binding.tilPassword.editText?.text.toString()

            if (email.isBlank()) {
                binding.tilEmail.error = "Inserisci l'email"
                return@setOnClickListener
            }

            // Logica di salvataggio "Ricordami"
            val editor = pref.edit()
            if (binding.cbRemember.isChecked) {
                editor.putString("email", email)
                editor.putString("pass", pass)
            } else {
                editor.clear()
            }
            editor.apply()

            // ROUTING DECISIVO
            if (email == "admin") {
                // Avvio sicuro AdminActivity
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            } else {
                // Avvio standard MainActivity per i clienti
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            
            // Chiude il login così non si può tornare indietro
            finish()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
