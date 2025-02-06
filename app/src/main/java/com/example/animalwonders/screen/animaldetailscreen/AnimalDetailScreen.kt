package com.example.animalwonders.screen.animaldetailscreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import com.example.animalwonders.viewmodel.AnimalViewModel

@Composable
fun AnimalDetailScreen(animalId: Int, animalViewModel: AnimalViewModel) {
    // Observe the selected animal by ID
    animalViewModel.fetchAnimalById(animalId)
    val animal by animalViewModel.selectedAnimal.observeAsState(initial = null)
    animal?.let {
        val imageScale = remember { Animatable(1f) }

        // Animate image scaling effect
        LaunchedEffect(Unit) {
            imageScale.animateTo(
                targetValue = 1.2f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Image(
                painter = painterResource(id = it.imageResId),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer(
                        scaleX = imageScale.value,
                        scaleY = imageScale.value
                    )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it.description,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
