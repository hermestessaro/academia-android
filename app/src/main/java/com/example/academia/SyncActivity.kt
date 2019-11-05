package com.example.academia

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.academia.controllers.WebService.RetrofitInitializer
import com.example.academia.models.ProfessorModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SyncActivity : AppCompatActivity() {

    val PRIVATE_MODE = 0
    val PREF_NAME = "synced"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)
        //this.deleteDatabase("database.db")

        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        //já sincronizou
        if (sharedPref.getBoolean(PREF_NAME, false)) {
            val builder = AlertDialog.Builder(applicationContext)
            builder.setTitle("Sincronizar")
            builder.setMessage("Você gostaria de sincronizar o banco de dados?")
            builder.setPositiveButton("Sim") { dialog, which ->
                sync()
            }
            builder.setNegativeButton("Não") { _, _ -> }
            builder.create().show()
        }
        //não sincronizou
        else {
            sync()
            val editor = sharedPref.edit()
            editor.putBoolean(PREF_NAME, true)
            editor.apply()
        }

        val intent = Intent(applicationContext, SelectTrainer::class.java)
        startActivity(intent)

    }

    fun sync() {
        val dbHelper = DatabaseHelper(this)
        val professors = callAPI(RetrofitInitializer().appServices().getProfessors())
        val aparelhos = callAPI(RetrofitInitializer().appServices().getAparelhos())
        if(aparelhos!=null){
            aparelhos.forEach {
                Log.d("aparelhos", it.nome)
            }
        }
    }


    fun <T:Any> callAPI(call: Call<List<T>>): List<T>? {
        var list:List<T>? = null

        call.enqueue(object : Callback<List<T>> {
            override fun onResponse(call: Call<List<T>>, response: Response<List<T>>) {
                Toast.makeText(applicationContext, "pei", Toast.LENGTH_LONG).show()
                list = response.body()
                /*professores!!.forEach {
                    dbHelper.createProfessor(it)
                }*/
            }

            override fun onFailure(call: Call<List<T>>, t: Throwable) {
                Toast.makeText(applicationContext, "deu ruim", Toast.LENGTH_LONG).show()
                Log.e("onFailure error", t.message!!)
            }
        })

        return list
    }
}




    /*fun <T: Any> getData(clazz: KClass<T>): List<T>? {
    return when(clazz) {
        User::class -> getUsers() as List<T>
        Student::class -> getStudents()  as List<T>
        else -> null
    }*/

