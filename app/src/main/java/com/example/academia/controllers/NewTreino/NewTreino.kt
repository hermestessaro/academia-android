package com.example.academia.controllers.NewTreino

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.academia.R
import com.example.academia.controllers.VisualizeTreino.VisualizeTreinoFragment

class NewTreino : AppCompatActivity() {

    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_treino)

        val new = intent.getBooleanExtra("NEW_TREINO", false)
        val visualizeTreinofrag = VisualizeTreinoFragment(new)
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.content_frame, visualizeTreinofrag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    companion object {
        fun start(context: Context, new: Boolean): Intent {
            val intent = Intent(context, NewTreino::class.java)
            intent.putExtra("NEW_TREINO", new)
            return intent
        }
    }
}