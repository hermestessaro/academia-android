package com.example.academia

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.academia.Database.DatabaseHelper
import com.example.academia.WebService.RetrofitInitializer
import com.example.academia.WebService.SyncHelper
import com.example.academia.models.AparelhoModel
import com.example.academia.models.ExercicioModel
import com.example.academia.models.GrupoModel
import com.example.academia.models.ProfessorModel
import kotlinx.android.synthetic.main.activity_sync.*
import kotlinx.coroutines.*
import java.lang.Runnable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FirstSyncActivity : AppCompatActivity() {

    val PRIVATE_MODE = 0
    val PREF_FILE = "prefFile"
    val PREF_NAME = "synced"
    val PREF_DATE = "last_sync"
    val dbHelper = DatabaseHelper(this)
    val syncHelper = SyncHelper(dbHelper)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)



        val sharedPref: SharedPreferences = getSharedPreferences(PREF_FILE, PRIVATE_MODE)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        //this.deleteDatabase("database.db")
        //sharedPref.edit().clear().apply()

        //variables to check if there's internet connection
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var result = false
        connectivityManager.run {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                result = when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }

        //já sincronizou
        if (!sharedPref.getBoolean(PREF_NAME, false)) {
            if(result) {
                runOnUiThread {
                    run {
                        if (!isFinishing) {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Sincronizar")
                            builder.setMessage("Você gostaria de sincronizar o banco de dados?")
                            builder.setPositiveButton("Sim") { dialog, which ->
                                first_sync_progress.visibility = View.VISIBLE
                                GlobalScope.launch {
                                    syncHelper.getAll().await()

                                    val editor = sharedPref.edit()
                                    editor.putBoolean(PREF_NAME, true)

                                    val date = LocalDateTime.now().format(formatter).toString()
                                    Log.d("date", date)
                                    editor.putString(PREF_DATE, date)
                                    editor.apply()

                                    val intent = Intent(applicationContext, SelectTrainer::class.java)
                                    startActivity(intent)

                                }


                            }
                            builder.setNegativeButton("Não") { _, _ ->
                                val prof = ProfessorModel(9, "ProfTeste", "teste@teste.com", "1234", "", "", "Sim")
                                dbHelper.createProfessor(prof)
                                dbHelper.createGrupo("Peito")
                                dbHelper.createGrupo("Costas")
                                dbHelper.createGrupo("Pernas")
                                dbHelper.createGrupo("Ombros")
                                dbHelper.createGrupo("Biceps")
                                dbHelper.createGrupo("Triceps")

                                val ap1 = AparelhoModel(1, "Supino")
                                val ap2 = AparelhoModel(2, "Remada baixa")
                                val ap3 = AparelhoModel(3, "Agachamento")
                                val ap4 = AparelhoModel(4, "Desenvolvimento")
                                val ap5 = AparelhoModel(5, "Barra reta")
                                val ap6 = AparelhoModel(6, "Francesa")
                                dbHelper.createAparelho(ap1)
                                dbHelper.createAparelho(ap2)
                                dbHelper.createAparelho(ap3)
                                dbHelper.createAparelho(ap4)
                                dbHelper.createAparelho(ap5)
                                dbHelper.createAparelho(ap6)
                                val intent = Intent(applicationContext, SelectTrainer::class.java)
                                startActivity(intent)
                            }
                            builder.create().show()
                        }
                    }
                }
            }


        }
        //não sincronizou
        else {
            /*GlobalScope.launch{
                syncHelper.getAll().await()
            }

            val editor = sharedPref.edit()
            editor.putBoolean(PREF_NAME, true)
            val date = LocalDateTime.now().toString()
            Log.d("date", date)
            editor.putString(PREF_DATE, date)
            editor.apply()*/
            val intent = Intent(applicationContext, SelectTrainer::class.java)
            startActivity(intent)
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
