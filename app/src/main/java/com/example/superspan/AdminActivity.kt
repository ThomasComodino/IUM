package com.example.superspan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.superspan.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.admin_nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        // LOGICA AVANZATA: Se Daniela è nel dettaglio di un candidato e preme "Lavoro",
        // l'app la riporta alla lista completa di tutti i candidati.
        binding.adminBottomNav.setOnItemSelectedListener { item ->
            val navOptions = androidx.navigation.navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = false
                }
                launchSingleTop = true
                restoreState = false
            }
            navController.navigate(item.itemId, null, navOptions)
            true
        }
    }
}