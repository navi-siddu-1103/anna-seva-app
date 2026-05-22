package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.data.AppDatabase
import com.example.data.RationRepository
import com.example.ui.RationAppUi
import com.example.ui.RationViewModel
import com.example.ui.RationViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialize local SQLite Room database with seeding logic
        val database = AppDatabase.getDatabase(this, lifecycleScope)
        val repository = RationRepository(database.rationDao())

        // 2. Instantiate View Model with Factory pattern for Dependency Injection
        val viewModelFactory = RationViewModelFactory(application, repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[RationViewModel::class.java]

        // 3. Render modern edge-to-edge Material Design 3 UI layer
        setContent {
            MyApplicationTheme {
                RationAppUi(viewModel)
            }
        }
    }
}
