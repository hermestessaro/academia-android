package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class ProfessorModel (val IdProfessor: Int,
                      val Nome: String,
                      val Email: String,
                      val Senha: String,
                      val DataInclusao: String,
                      val DataHoraUltimaAtu: String,
                      val IndicadorAtivo: String
): Parcelable