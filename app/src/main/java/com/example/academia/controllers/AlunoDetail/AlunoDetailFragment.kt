package com.example.academia.controllers.AlunoDetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.academia.DatabaseHelper
import com.example.academia.R
import com.example.academia.models.AlunoModel
import kotlinx.android.synthetic.main.fragment_aluno_detail.*
import kotlinx.android.synthetic.main.fragment_aluno_detail.view.*
import org.w3c.dom.Text

class AlunoDetailFragment() : Fragment() {

    lateinit var aluno: AlunoModel
    lateinit var dbHelper: DatabaseHelper


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = DatabaseHelper(activity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            this.aluno = arguments!!.getParcelable("alunoClicked")!!
        }
        Log.d("nome", this.aluno.nome)
        //setUpView()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_aluno_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val nome = view.findViewById<TextView>(R.id.nome_aluno)
        //nome.text = aluno.nome
        setUpView()

    }

    fun setUpView(){
        val dropdownObs = dropdown_obs
        val dropdownTreino = dropdown_treino_esp
        val dropdownLesoes = dropdown_lesoes
        val dropdownInd = dropdown_indicacoes


        nome_aluno.text = aluno.nome
        data_nascimento.text = aluno.dataNascimento
        prof.text = aluno.prof
        dropdownObs.setContentText(aluno.observacoes)
        dropdownLesoes.setContentText(aluno.lesoes)

    }

    companion object {
        fun newInstance(alunoClicked: AlunoModel): AlunoDetailFragment {
            val frag = AlunoDetailFragment()
            val args = Bundle()
            args.putParcelable("alunoClicked", alunoClicked)
            frag.arguments = args
            return frag
        }

    }
}