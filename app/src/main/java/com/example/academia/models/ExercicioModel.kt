package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class ExercicioModel(val idTreino: Int,
                     val idAparelho: Int,
                     val nome: String,
                     val series: Int,
                     val repeticoes: Int,
                     val peso: Int
)