package com.example.academia.controllers.AlunosLista

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.academia.DatabaseHelper
import com.example.academia.MainActivity
import com.example.academia.R
import com.example.academia.controllers.AlunoDetail.AlunoDetailFragment
import com.example.academia.models.AlunoModel
import kotlinx.android.synthetic.main.fragment_alunos_lista.*

class AlunosListaFragment : Fragment(), AlunoClick {

    //for tests only
    /*private val mAlunos = listOf(
        AlunoModel(0,"Grohe","02/02/70", "Renato"),
        AlunoModel(1,"Edilson","02/02/70", "Renato"),
        AlunoModel(2,"Geromel","02/02/70", "Renato"),
        AlunoModel(3,"Kannemann","02/02/70", "Renato"),
        AlunoModel(4,"Cortez","02/02/70", "Renato"),
        AlunoModel(5,"Miohel","02/02/70", "Renato"),
        AlunoModel(6,"Arthur","02/02/70", "Renato"),
        AlunoModel(7,"Ramiro","02/02/70", "Renato"),
        AlunoModel(8,"Luan","02/02/70", "Renato"),
        AlunoModel(9,"Fernando","02/02/70", "Renato"),
        AlunoModel(10,"Barrios","02/02/70", "Renato")

    )*/
    lateinit var dbHelper: DatabaseHelper
    lateinit var mAlunos: MutableList<AlunoModel>
    val frag = this


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = DatabaseHelper(activity)
        mAlunos = dbHelper.getAllAlunos()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
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
        val frag = AlunoDetailFragment.newInstance(aluno)
        main.supportFragmentManager.beginTransaction().replace(R.id.content_frame, frag).addToBackStack(null).commit()
    }

    companion object {
        fun newInstance(): AlunosListaFragment {
            return AlunosListaFragment()
        }
    }
}