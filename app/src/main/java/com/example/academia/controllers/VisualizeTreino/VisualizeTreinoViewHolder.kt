package com.example.academia.controllers.VisualizeTreino

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academia.R
import com.example.academia.models.ExercicioModel

class VisualizeTreinoViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.exercicio_lista_view, parent, false))  {

    private var mNameView: TextView? = null
    private var mInfoView: TextView? = null

    init {
        mNameView = itemView.findViewById(R.id.first_text_view)
        mInfoView = itemView.findViewById(R.id.second_text_view)
    }

    fun bind(exercicio: ExercicioModel, visualizeTreinoFrag: VisualizeTreinoFragment) {
        mNameView?.text = exercicio.nome
        val infoText = "Séries: ${exercicio.series} Reps: ${exercicio.repeticoes} Carga: ${exercicio.peso}"
        mInfoView?.text = infoText
        itemView.setOnClickListener{
            visualizeTreinoFrag.onExercicioClicked(exercicio)
        }
    }

}