package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class GrupoModel(val id: Int,
                 val nome: String
) : Parcelable