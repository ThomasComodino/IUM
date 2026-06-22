package com.example.superspan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.superspan.databinding.ActivityAdminBinding

/**
 * Attività principale per l'Area Admin (Daniela).
 * Gestisce la navigazione tramite Navigation Component.
 */
class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Inizializzazione ViewBinding
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Setup del Navigation Component
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.admin_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        
        // 3. Collegamento BottomNavigationView al NavController
        // In ViewBinding gli ID con underscore diventano camelCase (admin_bottom_nav -> adminBottomNav)
        binding.adminBottomNav.setupWithNavController(navController)
    }
}
