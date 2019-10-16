package com.example.academia.controllers.AlunosLista

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.academia.DatabaseHelper
import com.example.academia.MainActivity
import com.example.academia.PagerAdapter
import com.example.academia.R
import com.example.academia.controllers.AlunoDetail.AlunoDetailFragment
import com.example.academia.models.AlunoModel
import kotlinx.android.synthetic.main.fragment_alunos_lista.*

class MyAlunosListaFragment : Fragment(), AlunoClick  {
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
        val frag = AlunoDetailFragment.newInstance(aluno)
        main.supportFragmentManager.beginTransaction().replace(R.id.content_frame, frag).addToBackStack(null).commit()
    }

    companion object {
        fun newInstance(): MyAlunosListaFragment {
            return MyAlunosListaFragment()
        }
    }
}