package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class TreinoModel(val idTreino: Int,
                val idProf: Int,
                val idAluno: Int,
                val nome: String,
                val tipo: String
)