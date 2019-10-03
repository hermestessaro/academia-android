package com.example.academia.controllers.CreateTreino

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academia.models.ExercicioModel

class ExerciciosAdapter(private val list: List<ExercicioModel>, val createTreinoFrag: CreateTreinoFragment):
    RecyclerView.Adapter<CreateTreinoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateTreinoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CreateTreinoViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CreateTreinoViewHolder, position: Int) {
        val exercicio: ExercicioModel = list[position]
        holder.bind(exercicio, createTreinoFrag)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}