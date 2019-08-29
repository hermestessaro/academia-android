
package com.example.academia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class NewAluno : AppCompatActivity() {
    lateinit var db: DatabaseHelper
    private val DB_NAME = "database.db"
    private val DB_VERSION = 1

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_aluno)

        db = DatabaseHelper(applicationContext, DB_NAME, null, DB_VERSION)

        


    }
}