package com.example.academia.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlunoModel(
    val Nome: String,
    val DataNascimento: String,
    val Prof: String,
    val IndicadorDorPeitoAtividadesFisicas: Boolean,
    val dorPeitoUltimoMes: Boolean,
    val perdaConsciencia: Boolean,
    val problemaArticular: Boolean,
    val tabagista: Boolean,
    val diabetico: Boolean,
    val familiarCardiaco: Boolean,
    val lesoes: String,
    val observacoes: String,
    val treinoEspecifico: String,
    val dataInclusao: String,
    val indicadorAtivo: Boolean
) : Parcelable