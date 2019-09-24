package com.example.academia.controllers.VisualizeTreino

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academia.models.ExercicioModel

class ExerciciosAdapter(private val list: List<ExercicioModel>, val visualizeTreinoFrag: VisualizeTreinoFragment):
    RecyclerView.Adapter<VisualizeTreinoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisualizeTreinoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VisualizeTreinoViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: VisualizeTreinoViewHolder, position: Int) {
        val exercicio: ExercicioModel = list[position]
        holder.bind(exercicio, visualizeTreinoFrag)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}