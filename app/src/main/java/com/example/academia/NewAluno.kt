
package com.example.academia

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.academia.models.AlunoModel
import kotlinx.android.synthetic.main.activity_new_aluno.*


class NewAluno : AppCompatActivity() {
    private val DB_NAME = "database.db"
    private val DB_VERSION = 1

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_aluno)


        button_save.setOnClickListener {
            saveAluno()
            Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java) )
        }
        button_goto_training.setOnClickListener {
            saveAluno()
            Toast.makeText(this, "vai pro treino", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveAluno(){
        val dbHelper = DatabaseHelper(this)
        val nome = nome_et.text.toString()
        val data = data_et.text.toString()
        val dorPeitoAtividades = dorPeito_rg.checkedRadioButtonId == R.id.dorPeitoSim
        val dorPeitoMes = dorPeitoMes_rg.checkedRadioButtonId == R.id.dorPeitoMesSim
        val perdaConsciencia = consciencia_rg.checkedRadioButtonId == R.id.conscienciaSim
        val problemaOsseo = osseo_rg.checkedRadioButtonId == R.id.osseoSim
        val tabagista = tabagista_rg.checkedRadioButtonId == R.id.tabagistaSim
        val diabetico = diabetico_rg.checkedRadioButtonId == R.id.diabeticoSim
        val cardiaco = cardiaco_rg.checkedRadioButtonId == R.id.cardiacoSim
        val lesoes = lesoes_et.text.toString()
        val observacoes = obs_et.text.toString()

        val aluno = AlunoModel(nome, data, "prof", dorPeitoAtividades, dorPeitoMes, perdaConsciencia,
                                problemaOsseo, tabagista, diabetico, cardiaco, lesoes, observacoes, "")
        dbHelper.createAluno(aluno)

    }


}