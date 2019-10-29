package com.example.academia

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.academia.controllers.WebService.RetrofitInitializer

class SyncActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)

        val call = RetrofitInitializer().appServices().getProfessors()
        call.enqueue()
    }
}