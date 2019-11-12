package com.example.academia.controllers.AlunosLista

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.academia.Database.DatabaseHelper
import com.example.academia.MainActivity
import com.example.academia.R
import com.example.academia.controllers.AlunoDetail.AlunoDetailFragment
import com.example.academia.models.AlunoModel
import kotlinx.android.synthetic.main.fragment_alunos_lista.*

class MyAlunosListaFragment(val profName: String) : Fragment(), AlunoClick  {
    lateinit var dbHelper: DatabaseHelper
    lateinit var mAlunos: MutableList<AlunoModel>
    val frag = this



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = DatabaseHelper(activity)
        val prof = dbHelper.getProfessorByName(profName)
        Log.d("nomeprof", prof.Nome)
        mAlunos = dbHelper.getAlunosByIdProf(prof.IdProfessor)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    /*override fun onResume() {
        mAlunos = dbHelper.getAllAlunos()
        alunosRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = AlunosAdapter(mAlunos, frag as AllAlunosListaFragment)

        }
        super.onResume()
    }*/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_alunos_lista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alunosRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = AlunosAdapter(mAlunos, frag)

        }
    }

    override fun onAlunoClicked(aluno: AlunoModel) {
        val main = activity as MainActivity
        main.viewPagerDisappears()
        val frag = AlunoDetailFragment.newInstance(aluno)
        main.supportFragmentManager.beginTransaction().replace(R.id.content_frame, frag).addToBackStack(null).commit()
    }

    companion object {
        fun newInstance(profName: String): MyAlunosListaFragment {
            return MyAlunosListaFragment(profName)
        }
    }
}