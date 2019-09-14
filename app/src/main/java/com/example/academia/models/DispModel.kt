package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class DispModel(
    val seg: Boolean,
    val ter: Boolean,
    val qua: Boolean,
    val qui: Boolean,
    val sex: Boolean,
    val sab: Boolean,
    val dom: Boolean,
    val idAluno: Int
)