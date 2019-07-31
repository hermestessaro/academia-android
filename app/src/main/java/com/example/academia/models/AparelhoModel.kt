package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AparelhoModel(val id: Int,
                    val idGrupo: Int,
                    val nome: String
) : Parcelable