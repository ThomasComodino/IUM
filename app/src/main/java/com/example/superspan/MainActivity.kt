package com.example.superspan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.superspan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Troviamo il controller che gestisce la navigazione
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // LOGICA AVANZATA: Reset della navigazione al click sulla Navbar
        // Invece di setupWithNavController, usiamo un listener manuale
        binding.bottomNav.setOnItemSelectedListener { item ->
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