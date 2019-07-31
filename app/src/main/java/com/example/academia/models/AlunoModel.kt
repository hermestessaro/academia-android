package com.example.academia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlunoModel(val id: Int,
                 val nome: String,
                 val dataNascimento: String,
            val dorPeitoAtividades: Boolean,
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