package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FrequenciaModel(val id: Int,
                      val idTreino: Int,
                      val dataFreq: String
) : Parcelable