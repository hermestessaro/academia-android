package com.example.academia.controllers.AlunosLista

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.academia.models.AlunoModel

class AlunosAdapter(private val list: List<AlunoModel>, val alunoListFrag: Fragment):
    RecyclerView.Adapter<AlunosListaViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunosListaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AlunosListaViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: AlunosListaViewHolder, position: Int) {
        val aluno: AlunoModel = list[position]
        holder.bind(aluno, alunoListFrag)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}