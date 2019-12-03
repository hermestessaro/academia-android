package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlunoModelServer (
    var IdAluno: Int,
    val Nome: String,
    val DataNascimento: String,
    val IdProf: Int,
    val IndicadorDorPeitoAtividadesFisicas: String,
    val IndicadorDorPeitoUltimoMes: String,
    val IndicadorPerdaConscienciaTontura: String,
    val IndicadorProblemaArticular: String,
    val IndicadorTabagista: String,
    val IndicadorDiabetico: String,
    val IndicadorFamiliarAtaqueCardiaco: String,
    val Lesoes: String?,
    val Observacoes: String?,
    val TreinoEspecifico: String?,
    val DataInclusao: String,
    val DataHoraUltimaAtu: String,
    val IndicadorAtivo: String
) : Parcelable