package com.example.academia.controllers.AlunosLista

import com.example.academia.models.AlunoModel

interface AlunoClick {
    fun onAlunoClicked(aluno: AlunoModel)
}