package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class TreinoModel(val id: Int,
                  val idProf: Int,
                  val idAluno: Int,
                  val nome: String,
                  val objetivos: String,
                  val dataInc: String,
                  val dataUlt: String,
                  val indicadorAtivo: Boolean
)