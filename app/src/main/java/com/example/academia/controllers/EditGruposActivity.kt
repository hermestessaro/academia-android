package com.example.academia.controllers

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.academia.DatabaseHelper
import com.example.academia.R
import kotlinx.android.synthetic.main.activity_edit_grupos.*

class EditGruposActivity : AppCompatActivity(){

    internal lateinit var spinner: Spinner
    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_grupos)

        initView()



    }

    fun initView(){
        spinner = findViewById(R.id.grupos_spinner)
        dbHelper = DatabaseHelper(this)

        val grupos = dbHelper.getAllGrupos()
        val grupos_names = ArrayList<String>()
        for(item in grupos){
            grupos_names.add(item.nome)
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, grupos_names)
        spinner.adapter = adapter


        setSupportActionBar(toolbar)
        toolbar.title = "Adicionar exercício"
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}