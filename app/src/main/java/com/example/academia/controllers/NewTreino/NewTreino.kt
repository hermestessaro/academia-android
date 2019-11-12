package com.example.academia.controllers.NewTreino

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.academia.Database.DatabaseHelper
import com.example.academia.R
import com.example.academia.controllers.CreateTreino.CreateTreinoFragment
import com.example.academia.controllers.ViewTreino.ViewTreinoFragment
import kotlinx.android.synthetic.main.toolbar.*

class NewTreino : AppCompatActivity() {

    val manager = supportFragmentManager
    lateinit var toolbar_layout: androidx.appcompat.widget.Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_treino)

        val dbHelper = DatabaseHelper(this)

        toolbar_layout = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val new = intent.getBooleanExtra("NEW_TREINO", false)
        val clickedChild = intent.getIntExtra("treinoId", -1)
        val id = intent.getIntExtra("idAluno", -1)
        Log.d("clicked child", clickedChild.toString())

        //idTreino é igual a size porque não quero idTreino que seja 0, se não houver nenhum, vai ser 1
        //vai ser usado pra criação do treino no banco de dados

        val frag: Fragment

        //se new é true, o que veio no intent é o tamanho da lista de treinos
        if(new){
            val treinos = dbHelper.getTreinosByIdAluno(id)
            var idTreino = 0
            if(treinos.lastIndex != -1){
                val ids: MutableList<Int> = ArrayList()
                for(item in treinos){
                    ids.add(item.idTreino)
                }
                for(item in ids){
                    if(item > idTreino){
                        idTreino = item
                    }
                }

            }
            idTreino += 1
            frag = CreateTreinoFragment(new, idTreino, id)

        }
        //senao é só o id treino
        else{
            frag = ViewTreinoFragment(clickedChild, id)
        }

        val transaction = manager.beginTransaction()
        transaction.replace(R.id.content_frame, frag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        Log.d("entry_count", manager.backStackEntryCount.toString())
        if(manager.backStackEntryCount == 1){
            //finish()
            //faz nada, porque tem o botão pra cancelar no fragmento
        }
        else{
            super.onBackPressed()
        }
    }

    fun callExercicioDetail(nome: String, idTreino: Int, idAluno: Int?){
        val fragExercicioDetail = AddExercicioDialogFragment().newInstance(nome, idTreino, idAluno)
        fragExercicioDetail.show(manager.beginTransaction(), "dialog")
    }


    companion object {
        fun start(context: Context, new: Boolean, treinosSize: Int, idAluno: Int): Intent {
            val intent = Intent(context, NewTreino::class.java)
            intent.putExtra("NEW_TREINO", new)
            intent.putExtra("treinoId", treinosSize)
            intent.putExtra("idAluno", idAluno)
            return intent
        }
    }
}