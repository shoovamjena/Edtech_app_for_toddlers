package com.example.animalwonders

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.animalwonders.screen.homescreen.HomeScreen
import com.example.animalwonders.screen.loginscreen.LoginScreen
import com.example.animalwonders.screen.signupscreen.SignupScreen
import com.example.animalwonders.screen.welcomescreen.welcomeScreen
import com.example.animalwonders.viewmodel.AnimalViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationGraph(navController: NavHostController) {
    val navController = rememberNavController()

    // Firebase Auth instance to check the login state
    val auth = FirebaseAuth.getInstance()

    // Define the NavHost with the startDestination logic
    NavHost(
        navController = navController,
        startDestination = if (auth.currentUser != null) "home_screen" else "welcome_screen" // Check if user is logged in
    ) {
        // Welcome Screen
        composable("welcome_screen") {
            welcomeScreen(navController = navController)
        }

        // Home Screen
        composable("home_screen") {
            val animalViewModel: AnimalViewModel = viewModel()
            HomeScreen(
                viewModel = animalViewModel,
                onLogout = {
                    auth.signOut()
                    navController.navigate("welcome_screen") {
                        // Prevent back navigation to home screen
                        popUpTo("home_screen") { inclusive = true }
                    }
                },
                navController = navController
            )
        }

        // Signup Screen
        composable("signup_screen") {
            SignupScreen(navController = navController)
        }

        // Login Screen
        composable("login_screen") {
            LoginScreen(navController = navController)
        }
    }
}