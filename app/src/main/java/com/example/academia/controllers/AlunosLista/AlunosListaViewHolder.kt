package com.example.academia.controllers.AlunosLista

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.academia.R
import com.example.academia.models.AlunoModel

class AlunosListaViewHolder(inflater: LayoutInflater, parent: ViewGroup):
      RecyclerView.ViewHolder(inflater.inflate(R.layout.aluno_lista_view, parent, false)) {

    private var mNameView: TextView? = null
    private var mDateView: TextView? = null

    init {
        mNameView = itemView.findViewById(R.id.first_text_view)
        mDateView = itemView.findViewById(R.id.second_text_view)
    }

    fun bind(aluno: AlunoModel, alunoListFrag: Fragment) {
        mNameView?.text = aluno.nome
        mDateView?.text = aluno.dataNascimento
        itemView.setOnClickListener{
            alunoListFrag as AllAlunosListaFragment
            alunoListFrag.onAlunoClicked(aluno)
        }
    }
}