package com.example.animalwonders.screen.homescreen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun HomeScreen(
    navController: NavController,
    onLogout: () -> Unit
){
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    // State to store the user's name
    val userName = remember { mutableStateOf<String?>(null) }

    // Fetch the user's name from Firestore when the screen is first launched
    LaunchedEffect(auth.currentUser?.uid) {
        auth.currentUser?.uid?.let { userId ->
            try {
                val userDoc = firestore.collection("users").document(userId).get().await()
                userName.value = userDoc.getString("name") // Retrieve the name from Firestore
            } catch (e: Exception) {
                Toast.makeText(navController.context, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Display the user's name or a loading message if it's not available yet
    Column(modifier = Modifier.fillMaxWidth()) {
        if (userName.value != null) {
            Text(text = "Hi, ${userName.value}!")
        } else {
            Text(text = "Loading...")
        }

        Button(onClick = {
            onLogout() // Log out and navigate to WelcomeScreen
        }) {
            Text(text = "Logout")
        }
    }
}
