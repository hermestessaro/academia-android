package com.example.academia.controllers.CreateTreino

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.academia.models.ExercicioModel

class ExerciciosAdapter(private val list: List<ExercicioModel>, private val frag: Fragment, private val new: Boolean):
    RecyclerView.Adapter<ExerciciosListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciciosListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExerciciosListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ExerciciosListViewHolder, position: Int) {
        val exercicio: ExercicioModel = list[position]
        holder.bind(exercicio, frag, new)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}