package com.example.academia.controllers.NewTreino

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.academia.R
import com.example.academia.controllers.VisualizeTreino.VisualizeTreinoFragment

class NewTreino : AppCompatActivity() {

    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_treino)

        val new = intent.getBooleanExtra("NEW_TREINO", false)
        val size = intent.getIntExtra("treinosSize", -1)
        val id = intent.getIntExtra("idAluno", -1)
        Log.d("qte treinos", size.toString())

        //idTreino é igual a size porque não quero idTreino que seja 0, se não houver nenhum, vai ser 1
        //vai ser usado pra criação do treino no banco de dados
        val idTreino = size
        val visualizeTreinofrag = VisualizeTreinoFragment(new, idTreino, id)
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.content_frame, visualizeTreinofrag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun callExercicioDetail(nome: String, idTreino: Int, idAluno: Int?){
        val fragExercicioDetail = AddExercicioDialogFragment().newInstance(nome, idTreino, idAluno)
        fragExercicioDetail.show(manager.beginTransaction(), "dialog")
    }


    companion object {
        fun start(context: Context, new: Boolean, treinosSize: Int, idAluno: Int): Intent {
            val intent = Intent(context, NewTreino::class.java)
            intent.putExtra("NEW_TREINO", new)
            intent.putExtra("treinosSize", treinosSize)
            intent.putExtra("idAluno", idAluno)
            return intent
        }
    }
}