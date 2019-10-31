package com.example.academia

import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)
        //this.deleteDatabase("database.db")
        val dbHelper = DatabaseHelper(this)

        val call = RetrofitInitializer().appServices().getProfessors()
        call.enqueue(object: Callback<List<ProfessorModel>>{
            override fun onResponse(call: Call<List<ProfessorModel>>, response: Response<List<ProfessorModel>>) {
                Toast.makeText(applicationContext, "pei", Toast.LENGTH_LONG).show()
                val professores = response.body()

                professores!!.forEach {
                    dbHelper.createProfessor(it)
                }
                val intent = Intent(applicationContext, SelectTrainer::class.java)
                startActivity(intent)
            }

            override fun onFailure(call: Call<List<ProfessorModel>>, t: Throwable) {
                Toast.makeText(applicationContext, "deu ruim", Toast.LENGTH_LONG).show()
                Log.e("onFailure error", t?.message)
            }
        })
    }
}