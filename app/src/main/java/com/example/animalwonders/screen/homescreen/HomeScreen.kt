package com.example.animalwonders.screen.homescreen

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.animalwonders.ui.theme.PrimaryPink
import com.example.animalwonders.ui.theme.PrimaryPinkBlended
import com.example.animalwonders.ui.theme.PrimaryVioletDark
import com.example.animalwonders.uicomponents.home.TimeBasedGreeting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onLogout: () -> Unit
){
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    // State to store the user's name
    val userName = remember { mutableStateOf<String?>(null) }

    val gradientOffset by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 4000, easing = LinearEasing), label = "",
    )

    val gradientColors = listOf(PrimaryPinkBlended, PrimaryPink)
    val animatedBrush = Brush.verticalGradient(
        colors = gradientColors,
        startY = 0f,
        endY = with(LocalDensity.current) { 1000.dp.toPx() * gradientOffset },
        tileMode = TileMode.Clamp
    )

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
    Column(modifier = modifier
        .fillMaxSize()
        .background(animatedBrush)
        .systemBarsPadding()
        .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        if (userName.value != null) {
            TimeBasedGreeting()
            Text(
                text = "${userName.value}",
                color = PrimaryVioletDark,
                style = MaterialTheme.typography.headlineLarge
            )
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
