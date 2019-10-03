package com.example.academia.controllers.CreateTreino

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academia.R
import com.example.academia.models.ExercicioModel

class CreateTreinoViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.exercicio_lista_view, parent, false))  {

    private var mNameView: TextView? = null
    private var mInfoView: TextView? = null

    init {
        mNameView = itemView.findViewById(R.id.first_text_view)
        mInfoView = itemView.findViewById(R.id.second_text_view)
    }

    fun bind(exercicio: ExercicioModel, createTreinoFrag: CreateTreinoFragment) {
        mNameView?.text = exercicio.nomeAparelho
        var infoText = ""
        if(exercicio.nomeAparelho != "Adicionar Exercício"){
            infoText = "Séries: ${exercicio.series} Reps: ${exercicio.repeticoes} Carga: ${exercicio.peso}"
        }
        mInfoView?.text = infoText
        itemView.setOnClickListener{
            createTreinoFrag.onExercicioClicked(exercicio)
        }
    }

}