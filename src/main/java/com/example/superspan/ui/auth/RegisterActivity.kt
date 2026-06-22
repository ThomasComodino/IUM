package com.example.superspan.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.superspan.MainActivity
import com.example.superspan.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDoRegister.setOnClickListener {
            val nome = binding.tilRegNome.editText?.text.toString()
            val email = binding.tilRegEmail.editText?.text.toString()
            val pass = binding.tilRegPass.editText?.text.toString()

            // Validazione: non permette di andare avanti se i campi sono vuoti
            if (nome.isBlank() || email.isBlank() || pass.isBlank()) {
                if (nome.isBlank()) binding.tilRegNome.error = "Manca il nome" else binding.tilRegNome.error = null
                if (email.isBlank()) binding.tilRegEmail.error = "Manca l'email" else binding.tilRegEmail.error = null
                if (pass.isBlank()) binding.tilRegPass.error = "Manca la password" else binding.tilRegPass.error = null
                Toast.makeText(this, "Riempi tutti i campi!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registrazione completata per $nome!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}