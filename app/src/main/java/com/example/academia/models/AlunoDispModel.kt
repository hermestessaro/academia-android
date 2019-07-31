package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlunoDispModel(val id: Int,
                     val idAluno: Int,
                     val idDisp: Int
) : Parcelable
