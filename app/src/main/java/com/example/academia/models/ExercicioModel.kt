package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ExercicioModel(val id: Int,
                     val idTreino: Int,
                     val idAparelho: Int,
                     val series: Int,
                     val repeticoes: Int,
                     val peso: Int,
                     val dataUlt: String,
                     val indicadorAtivo: Boolean
) : Parcelable