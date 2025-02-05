package com.example.animalwonders.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.animalwonders.R
import com.example.animalwonders.room.Animal
import com.example.animalwonders.room.AnimalDatabase
import com.example.animalwonders.room.AnimalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnimalViewModel(application: Application) : AndroidViewModel(application) {

    private val animalRepository: AnimalRepository

    init {
        val animalDao = AnimalDatabase.getDatabase(application).animalDao()
        animalRepository = AnimalRepository(animalDao)

        addStaticAnimals()
    }

    private val _animals = MutableLiveData<List<Animal>>()
    val animals: LiveData<List<Animal>> get() = _animals

    private fun addStaticAnimals() {
        viewModelScope.launch(Dispatchers.IO) {
            // Check if there are any animals in the WILD category
            val wildAnimals = animalRepository.getAnimalsByCategory("WILD")
            if (wildAnimals.isEmpty()) {
                // Add static wild animals if no wild animals exist in the database
                val staticWildAnimals = listOf(
                    Animal(name = "Lion", imageResId = R.drawable.lion, category = "WILD", description = "This is a lion"),
                    Animal(name = "Tiger", imageResId = R.drawable.tiger, category = "WILD", description = "This is a tiger"),
                    Animal(name = "Elephant", imageResId = R.drawable.elephant, category = "WILD", description = "This is an elephant")
                )
                staticWildAnimals.forEach { animal ->
                    animalRepository.addAnimal(animal)
                }
            }

            // Check if there are any animals in the DOMESTIC category
            val domesticAnimals = animalRepository.getAnimalsByCategory("DOMESTIC")
            if (domesticAnimals.isEmpty()) {
                // Add static domestic animals if no domestic animals exist in the database
                val staticDomesticAnimals = listOf(
                    Animal(name = "Dog", imageResId = R.drawable.dog, category = "DOMESTIC", description = "This is a dog"),
                    Animal(name = "Cat", imageResId = R.drawable.cat, category = "DOMESTIC", description = "This is a cat")
                )
                staticDomesticAnimals.forEach { animal ->
                    animalRepository.addAnimal(animal)
                }
            }
        }
    }

    fun fetchAnimalsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO){
            val fetchedAnimals = animalRepository.getAnimalsByCategory(category)
            _animals.postValue(fetchedAnimals)
        }
    }

    fun deleteAnimal(animal: Animal) {
        viewModelScope.launch {
            animalRepository.deleteAnimal(animal)
        }
    }

}
