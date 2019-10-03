package com.example.academia.controllers.CreateTreino

import com.example.academia.models.ExercicioModel

interface ExercicioClick {
    fun onExercicioClicked(exercicio: ExercicioModel)
}