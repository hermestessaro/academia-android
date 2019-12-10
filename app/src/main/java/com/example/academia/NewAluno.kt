
package com.example.academia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.academia.Database.DatabaseHelper
import com.example.academia.models.AlunoModel
import com.example.academia.models.DispModel
import kotlinx.android.synthetic.main.activity_new_aluno.*
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NewAluno : AppCompatActivity() {

    lateinit var profName: String

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_aluno)
        profName = intent.getStringExtra("profName")!!

        DateInputMask(data_et).listen()

        button_save.setOnClickListener {
            checkDateFormat()
        }
        button_goto_training.setOnClickListener {
            saveAluno()
            Toast.makeText(this, "Treino", Toast.LENGTH_SHORT).show()
        }

        //hide soft keyboard
        dorPeito_rg.setOnCheckedChangeListener { group, checkedId ->
            val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        }
    }

    fun checkDateFormat(){
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        try{
            var date = LocalDate.parse(data_et.text.toString(), formatter)
            saveAluno()
            Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("profName", profName)
            startActivity(intent)
        }
        catch (e: Exception){
            Toast.makeText(applicationContext, "Data inválida! Lembre que o formato é dd/mm/aaaa", Toast.LENGTH_LONG).show()
            return
        }
    }


    fun saveAluno(){
        val dbHelper = DatabaseHelper(this)
        val nome = nome_et.text.toString()

        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var date = LocalDate.parse(data_et.text.toString(), formatter)
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        date = LocalDate.parse(date.toString(), formatter)
        val data = date.toString()

        val dorPeitoAtividades = dorPeito_rg.checkedRadioButtonId == R.id.dorPeitoSim
        val dorPeitoMes = dorPeitoMes_rg.checkedRadioButtonId == R.id.dorPeitoMesSim
        val perdaConsciencia = consciencia_rg.checkedRadioButtonId == R.id.conscienciaSim
        val problemaOsseo = osseo_rg.checkedRadioButtonId == R.id.osseoSim
        val tabagista = tabagista_rg.checkedRadioButtonId == R.id.tabagistaSim
        val diabetico = diabetico_rg.checkedRadioButtonId == R.id.diabeticoSim
        val cardiaco = cardiaco_rg.checkedRadioButtonId == R.id.cardiacoSim
        val lesoes = lesoes_et.text.toString()
        val observacoes = obs_et.text.toString()

        val prof = dbHelper.getProfessorByName(profName)
        val idAluno = dbHelper.getLastIdInsertedAluno()

        val aluno = AlunoModel(idAluno, nome, data, prof!!.IdProfessor, dorPeitoAtividades, dorPeitoMes, perdaConsciencia,
                    problemaOsseo, tabagista, diabetico, cardiaco, lesoes, observacoes, "",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString(), "", "1")
        dbHelper.createAluno(aluno)

        val disp = getDisp(dbHelper.getIdAlunoByName(nome))

        val teste = dbHelper.saveDisp(disp)
        Log.d("savedisp", teste.toString())

    }

    fun getDisp(id: Int): DispModel{
        val disp = DispModel(check_segunda.isChecked, check_terca.isChecked,
            check_quarta.isChecked, check_quinta.isChecked,
            check_sexta.isChecked, check_sabado.isChecked,
            check_domingo.isChecked, id)

        return disp

    }


    companion object {
        fun start(context: Context, prof: String): Intent {
            val intent = Intent(context, NewAluno::class.java)
            intent.putExtra("profName", prof)
            return intent
        }
    }

}