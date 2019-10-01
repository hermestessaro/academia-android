package com.example.academia.controllers.VisualizeTreino

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.academia.DatabaseHelper
import com.example.academia.R
import com.example.academia.controllers.GruposLista.GruposListaFragment
import com.example.academia.controllers.NewTreino.AddExercicioDialogFragment
import com.example.academia.controllers.NewTreino.NewTreino
import com.example.academia.models.ExercicioModel
import kotlinx.android.synthetic.main.fragment_visualize_treino.*

class VisualizeTreinoFragment(val newTreino: Boolean, val idTreino: Int, val idAluno: Int) : Fragment(), ExercicioClick {

    lateinit var dbHelper: DatabaseHelper
    var mExercicios: MutableList<ExercicioModel> = ArrayList()
    val frag = this


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = DatabaseHelper(activity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("idTreino", idTreino.toString())
        mExercicios = dbHelper.getExerciciosByIdTreino(idTreino)
        mExercicios.add(ExercicioModel(-1, "Adicionar Exercício", -1, -1, -1))
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_visualize_treino, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciciosRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ExerciciosAdapter(mExercicios, frag)
        }

        salvar_treino.setOnClickListener {
            saveTreino()
        }
    }

    fun saveTreino(){
        /*val idTreino: Int,
        val idProf: Int,
        val idAluno: Int,
        val nome: String,
        val tipo: String*/
    }

    override fun onExercicioClicked(exercicio: ExercicioModel){
        if(exercicio.nomeAparelho.equals("Adicionar Exercício")){
            val main = activity as NewTreino
            main.supportFragmentManager.beginTransaction().replace(R.id.content_frame, GruposListaFragment(true, idTreino)).addToBackStack(null).commit()
        }
        else{
            Toast.makeText(context, "não é o pei dos guri", Toast.LENGTH_LONG).show()
        }
    }

}