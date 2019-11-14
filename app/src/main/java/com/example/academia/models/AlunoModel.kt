package com.example.academia.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class AlunoModel(
    val Nome: String,
    val DataNascimento: String,
    val IdProf: Int,
    val IndicadorDorPeitoAtividadesFisicas: Boolean,
    val IndicadorDorPeitoUltimoMes: Boolean,
    val IndicadorPerdaConscienciaTontura: Boolean,
    val IndicadorProblemaArticular: Boolean,
    val IndicadorTabagista: Boolean,
    val IndicadorDiabetico: Boolean,
    val IndicadorFamiliarAtaqueCardiaco: Boolean,
    val Lesoes: String?,
    val Observacoes: String?,
    val TreinoEspecifico: String?,
    val DataInclusao: String,
    val DataHoraUltimaAtu: String,
    val IndicadorAtivo: String
) : Parcelable