package com.example.academia.controllers.AlunosLista

import androidx.fragment.app.Fragment
import com.example.academia.models.AlunoModel

class MyAlunosListaFragment : Fragment(), AlunoClick  {
    override fun onAlunoClicked(aluno: AlunoModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun newInstance(): MyAlunosListaFragment {
            return MyAlunosListaFragment()
        }
    }
}