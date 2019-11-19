package com.example.academia.controllers.ViewTreino

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.academia.Database.DatabaseHelper
import com.example.academia.R
import com.example.academia.controllers.CreateTreino.CreateTreinoFragment
import com.example.academia.controllers.CreateTreino.ExerciciosAdapter
import com.example.academia.controllers.NewTreino.NewTreino
import com.example.academia.models.ExercicioModel
import com.example.academia.models.TreinoModel
import kotlinx.android.synthetic.main.fragment_view_treino.*

class ViewTreinoFragment(val idTreino: Int, val idAluno: Int?) : Fragment() {
    lateinit var dbHelper: DatabaseHelper
    var mExercicios: MutableList<ExercicioModel> = ArrayList()
    val frag = this
    lateinit var treino: TreinoModel


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = DatabaseHelper(activity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        treino = dbHelper.getTreinoById(idTreino, idAluno!!)
        mExercicios = dbHelper.getExerciciosByIdTreino(idTreino, idAluno)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_view_treino, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nome_aluno.text = treino.nome
        tipo_treino.text = treino.tipo

        val aluno = dbHelper.getAlunoById(idAluno!!)
        nome_aluno.text = aluno!!.Nome

        exerciciosRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ExerciciosAdapter(mExercicios, frag, false)
        }

        voltar.setOnClickListener {
            activity!!.finish()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_view_treino, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.excluir_treino -> {
                excluirTreino()
                return true
            }
            R.id.editar_treino -> {
                editarTreino()
                return true
            }

            else ->
                super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    fun excluirTreino(){

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Excluir treino")
        builder.setMessage("Tem certeza que gostaria de excluir este treino e seus exercícios?")
        builder.setPositiveButton("Sim"){dialog, which ->
            dbHelper.deleteTreino(idAluno!!, idTreino)
            val main = activity as NewTreino
            main.supportFragmentManager.popBackStack()
            main.finish()
        }
        builder.setNegativeButton("Não"){_, _ ->}

        builder.create().show()


    }

    fun editarTreino(){
        val main = activity as NewTreino
        main.supportFragmentManager.beginTransaction().replace(R.id.content_frame, CreateTreinoFragment(false, idTreino, idAluno)).addToBackStack(null).commit()

    }
}