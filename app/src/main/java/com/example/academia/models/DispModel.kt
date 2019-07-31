package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DispModel(val id: Int,
                val nome: String
) : Parcelable