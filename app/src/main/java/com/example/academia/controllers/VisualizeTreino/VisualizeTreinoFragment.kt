package com.example.academia.controllers.VisualizeTreino

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.academia.DatabaseHelper
import com.example.academia.R
import com.example.academia.controllers.GruposLista.GruposListaFragment
import com.example.academia.controllers.NewTreino.AddExercicioDialogFragment
import com.example.academia.controllers.NewTreino.NewTreino
import com.example.academia.models.ExercicioModel
import com.example.academia.models.TreinoModel
import kotlinx.android.synthetic.main.fragment_visualize_treino.*

class VisualizeTreinoFragment(val newTreino: Boolean, var idTreino: Int, val idAluno: Int?) : Fragment(), ExercicioClick {

    lateinit var dbHelper: DatabaseHelper
    var mExercicios: MutableList<ExercicioModel> = ArrayList()
    val frag = this
    lateinit var treino: TreinoModel


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = DatabaseHelper(activity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        if(!newTreino){
            treino = dbHelper.getTreinoById(idTreino, idAluno!!)
        }
        Log.d("idTreino", idTreino.toString())
        mExercicios = dbHelper.getExerciciosByIdTreino(idTreino, idAluno!!)
        mExercicios.add(ExercicioModel(-1, -1,"Adicionar Exercício", -1, -1, -1))
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        mExercicios = dbHelper.getExerciciosByIdTreino(idTreino, idAluno!!)
        mExercicios.add(ExercicioModel(-1, -1,"Adicionar Exercício", -1, -1, -1))
        exerciciosRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ExerciciosAdapter(mExercicios, frag)
        }
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_visualize_treino, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!newTreino){
            nome_et.setText(treino.nome)
            tipo_et.setText(treino.tipo)
        }

        val spinner = alunos_spinner
        val alunos_names = ArrayList<String>()
        if(idAluno != null){
            val aluno = dbHelper.getAlunoById(idAluno)
            alunos_names.add(aluno.nome)
        }
        else{
            val alunos = dbHelper.getAllAlunos()
            for(item in alunos){
                alunos_names.add(item.nome)
            }
        }

        val spinner_adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, alunos_names)
        spinner.adapter = spinner_adapter

        exerciciosRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ExerciciosAdapter(mExercicios, frag)
        }

        salvar_treino.setOnClickListener {
            saveTreino()
            activity!!.finish()
        }
    }

    fun saveTreino(){
        val aluno_nome = alunos_spinner.selectedItem.toString()
        val treino_nome = nome_et.text.toString()
        val tipo_treino = tipo_et.text.toString()
        var auxIdAluno = -1
        if(idAluno==null){
            auxIdAluno = dbHelper.getIdAlunoByName(aluno_nome)
        }
        else{
            auxIdAluno = idAluno
        }

        val treino = TreinoModel(idTreino, 0, auxIdAluno, treino_nome, tipo_treino)
        dbHelper.saveTreino(treino)
    }

    override fun onExercicioClicked(exercicio: ExercicioModel){
        if(exercicio.nomeAparelho.equals("Adicionar Exercício")){
            val main = activity as NewTreino
            main.supportFragmentManager.beginTransaction().replace(R.id.content_frame, GruposListaFragment(true, idTreino, idAluno)).addToBackStack(null).commit()
        }
        else{
            Toast.makeText(context, "não é o pei dos guri", Toast.LENGTH_LONG).show()
        }
    }


}