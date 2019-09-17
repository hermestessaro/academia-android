package com.example.academia.controllers.AlunoDetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.academia.DatabaseHelper
import com.example.academia.R
import com.example.academia.models.AlunoModel

class AlunoDetailFragment : Fragment() {

    lateinit var aluno: AlunoModel
    lateinit var dbHelper: DatabaseHelper

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = DatabaseHelper(activity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_aluno_detail, container, false)
    }

    fun setAlunoDetail(alunoClicked: AlunoModel){
        aluno = alunoClicked
    }
}