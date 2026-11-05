package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.auth.AuthManager
import com.example.data.repository.CampaignReportRepository
import com.example.data.repository.FirebaseAiRepository
import com.example.ui.GramAwazApp
import com.example.ui.theme.GramAwazTheme
import com.example.ui.viewmodel.GramAwazViewModel
import com.example.util.configureAppCheck
import kotlinx.coroutines.MainScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Configure App Check with launch intent before any UI or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        val authManager = AuthManager(this)
        val firestoreRepository = CampaignReportRepository(this)
        val aiRepository = FirebaseAiRepository()
        val viewModel = GramAwazViewModel(
            aiRepository = aiRepository,
            firestoreRepository = firestoreRepository
        )

        // Attempt silent auto-sign in if previously authorized
        authManager.attemptAutoSignIn(
            scope = MainScope(),
            onAuthSuccess = { user ->
                // User signed in
            },
            onUnauthenticated = {
                // Stay unauthenticated until user signs in interactively
            }
        )

        setContent {
            GramAwazTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GramAwazApp(
                        viewModel = viewModel,
                        authManager = authManager
                    )
                }
            }
        }
    }
}
