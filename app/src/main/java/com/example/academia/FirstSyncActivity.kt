package com.example.academia

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.academia.WebService.RetrofitInitializer
import com.example.academia.models.AparelhoModel
import com.example.academia.models.GrupoModel
import com.example.academia.models.ProfessorModel
import kotlinx.coroutines.*
import java.lang.Runnable

class FirstSyncActivity : AppCompatActivity() {

    val PRIVATE_MODE = 0
    val PREF_NAME = "synced"
    val dbHelper = DatabaseHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)
        //this.deleteDatabase("database.db")

        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        //já sincronizou
        if (sharedPref.getBoolean(PREF_NAME, false)) {
            runOnUiThread(Runnable {
                kotlin.run {
                    if(!isFinishing){
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Sincronizar")
                        builder.setMessage("Você gostaria de sincronizar o banco de dados?")
                        builder.setPositiveButton("Sim") { dialog, which ->
                            GlobalScope.launch (Dispatchers.Default){
                                async{syncAll()}.await()
                            }
                        }
                        builder.setNegativeButton("Não") { _, _ ->
                            val prof = ProfessorModel(9, "ProfTeste", "teste@teste.com", "1234", "", "", "Sim")
                            dbHelper.createProfessor(prof)
                            val intent = Intent(applicationContext, SelectTrainer::class.java)
                            startActivity(intent)
                        }
                        builder.create().show()
                    }
                }
            })

        }
        //não sincronizou
        else {
            GlobalScope.launch (Dispatchers.IO){
                async{syncAll()}.await()

            }

            val editor = sharedPref.edit()
            editor.putBoolean(PREF_NAME, true)
            editor.apply()
            val intent = Intent(applicationContext, SelectTrainer::class.java)
            startActivity(intent)
        }



    }

     fun syncAll(){
        syncAparelhos()
        syncProf()
        syncGrupos()
    }



    fun syncProf() {
        GlobalScope.launch (Dispatchers.IO){
            try {
                val professors = retrieveProfessors().await() as List<ProfessorModel>
                withContext(Dispatchers.Main) {
                    professors.forEach {
                        val prof = ProfessorModel(
                            it.IdProfessor,
                            it.Nome,
                            it.Email,
                            it.Senha,
                            it.DataInclusao,
                            it.DataHoraUltimaAtu,
                            it.IndicadorAtivo
                        )
                        Log.d("name", prof.Nome)
                        dbHelper.createProfessor(prof)
                    }
                    val intent = Intent(applicationContext, SelectTrainer::class.java)
                    startActivity(intent)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    fun retrieveProfessors(): Deferred<Any> {
        val retrofit = RetrofitInitializer()

        return GlobalScope.async {
            try {
                val primaryResponse = retrofit.appServices().getProfessors().await()
                return@async primaryResponse.items


                /*if(primaryResponse.isSuccessful){
                    professors = primaryResponse.body()!!.items
                    return@async professors
                }
                else{
                    return@async null
                }*/
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun syncGrupos() {
        GlobalScope.launch (Dispatchers.IO) {
            try {
                val grupos = retrieveGrupos().await() as List<GrupoModel>
                withContext(Dispatchers.Main) {
                    grupos.forEach {
                        val grupo = GrupoModel(
                            it.IdGrupo,
                            it.Nome
                        )
                        Log.d("name", grupo.Nome)
                        dbHelper.createGrupo(grupo.Nome)
                    }
                    val intent = Intent(applicationContext, SelectTrainer::class.java)
                    startActivity(intent)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    fun retrieveGrupos(): Deferred<Any> {
        val retrofit = RetrofitInitializer()

        return GlobalScope.async {
            try {
                val primaryResponse = retrofit.appServices().getGrupos().await()
                return@async primaryResponse.items
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun syncAparelhos() {
        GlobalScope.launch (Dispatchers.IO) {
            try {
                val aparelhos = retrieveAparelhos().await() as List<AparelhoModel>

                withContext(Dispatchers.Main) {
                    aparelhos.forEach {
                        Log.d("nomeaparelho", it.Nome)
                        val aparelho = AparelhoModel(
                            it.IdGrupo,
                            it.Nome
                        )
                        dbHelper.createAparelho(aparelho)
                    }
                    val intent = Intent(applicationContext, SelectTrainer::class.java)
                    startActivity(intent)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    fun retrieveAparelhos(): Deferred<Any> {
        val retrofit = RetrofitInitializer()

        return GlobalScope.async {
            try {
                val primaryResponse = retrofit.appServices().getAparelhos(1).await()
                val items = primaryResponse.items
                var result = items
                if(primaryResponse._meta.pageCount > 1){
                    for(i in 2 until primaryResponse._meta.pageCount+1){
                        result = result + retrofit.appServices().getAparelhos(i).await().items
                    }
                }
                return@async result
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }




}


















/*val calls = ApiCalls()
        val professors = calls.getListApi(RetrofitInitializer().appServices().getProfessors())
        professors?.forEach {
            val prof = ProfessorModel(it.IdProfessor, it.Nome, it.Email, it.Senha, it.DataInclusao, it.DataHoraUltimaAtu, it.IndicadorAtivo)
            dbHelper.createProfessor(prof)
        }*/
//val aparelhos = calls.getListApi(RetrofitInitializer().appServices().getAparelhos())
