package com.example.academia.controllers.AlunosLista

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.academia.R
import com.example.academia.models.AlunoModel
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AlunosListaViewHolder(inflater: LayoutInflater, parent: ViewGroup):
      RecyclerView.ViewHolder(inflater.inflate(R.layout.aluno_lista_view, parent, false)) {

    private var mNameView: TextView? = null
    private var mDateView: TextView? = null

    init {
        mNameView = itemView.findViewById(R.id.first_text_view)
        mDateView = itemView.findViewById(R.id.second_text_view)
    }

    fun bind(aluno: AlunoModel, alunoListFrag: Fragment) {
        mNameView?.text = aluno.Nome

        val date = LocalDate.parse(aluno.DataNascimento) // data em formato yyyy-MM-dd
        val date_text: String = date.dayOfMonth.toString() +"/"+ date.monthValue.toString() +"/"+ date.year.toString()
        mDateView?.text = date_text
        itemView.setOnClickListener{
            if(alunoListFrag is AllAlunosListaFragment){
                alunoListFrag.onAlunoClicked(aluno)
            }
            if(alunoListFrag is MyAlunosListaFragment){
                alunoListFrag.onAlunoClicked(aluno)
            }

        }
    }
}