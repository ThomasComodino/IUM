package com.example.superspan.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.superspan.data.FakeRepository
import com.example.superspan.data.User
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
                if (nome.isBlank()) binding.tilRegNome.error = "Campo obbligatorio" else binding.tilRegNome.error = null
                if (email.isBlank()) binding.tilRegEmail.error = "Campo obbligatorio" else binding.tilRegEmail.error = null
                if (pass.isBlank()) binding.tilRegPass.error = "Campo obbligatorio" else binding.tilRegPass.error = null
                Toast.makeText(this, "Riempi tutti i campi!", Toast.LENGTH_SHORT).show()
            } else {
                val success = FakeRepository.registerUser(User(nome, email, pass))
                if (success) {
                    Toast.makeText(this, "Registrazione completata per $nome!", Toast.LENGTH_SHORT).show()
                    // Invece della Home, apriamo il Login come richiesto
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Riutilizza la LoginActivity esistente se presente
                    startActivity(intent)
                    finish()
                } else {
                    binding.tilRegEmail.error = "Email già registrata"
                    Toast.makeText(this, "Errore: Email già esistente!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}