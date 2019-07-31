package com.example.academia
import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var db: DatabaseHelper
    private val DB_NAME = "database.db"
    private val DB_VERSION = 1

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        db = DatabaseHelper(applicationContext, DB_NAME, null, DB_VERSION)

        //TODO: SYNC

    }

}