package com.example.academia.controllers.VisualizeTreino

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.academia.DatabaseHelper
import com.example.academia.R
import com.example.academia.models.ExercicioModel
import kotlinx.android.synthetic.main.fragment_visualize_treino.*

class VisualizeTreinoFragment : Fragment(), ExercicioClick {

    lateinit var dbHelper: DatabaseHelper
    //lateinit var mExercicios: MutableList<ExercicioModel>
    val frag = this

    //tests
    private val mExercicios = listOf(
        ExercicioModel(1,1, "Supino vertical", 4, 8, 30),
        ExercicioModel(1, 2, "Supino inclinado", 4, 8, 20),
        ExercicioModel(-1, -1, "Adicionar Exercício", -1, -1, -1)
    )

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = DatabaseHelper(activity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
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
    }

    override fun onExercicioClicked(exercicio: ExercicioModel){
        if(exercicio.nome.equals("Adicionar Exercício")){
            Toast.makeText(context, "PEI DOS GURI", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(context, "não é o pei dos guri", Toast.LENGTH_LONG).show()
        }
    }
}