package com.example.superspan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.superspan.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nota: dovrai creare anche admin_nav_graph.xml per Daniela
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.admin_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        binding.adminBottomNav.setupWithNavController(navController)
    }
}