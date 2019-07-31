package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class ProfessorModel (val id: Int,
                      val nome: String,
                      val email: String,
                      val senha: String,
                      val dataInc: String,
                      val dataUlt: String,
                      val indicadorAtivo: Boolean
): Parcelable