package com.example.academia.controllers.VisualizeTreino

import com.example.academia.models.ExercicioModel

interface ExercicioClick {
    fun onExercicioClicked(exercicio: ExercicioModel)
}