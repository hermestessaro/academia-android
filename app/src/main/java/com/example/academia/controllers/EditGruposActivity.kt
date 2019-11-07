package com.example.academia.controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.academia.DatabaseHelper
import com.example.academia.R
import com.example.academia.controllers.GruposLista.GruposListaFragment
import com.example.academia.models.AparelhoModel
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
            grupos_names.add(item.Nome)
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, grupos_names)
        spinner.adapter = adapter


        setSupportActionBar(toolbar)
        toolbar.title = "Adicionar exercício"
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        salvar_exercicio.setOnClickListener {
            saveExercicio()
        }
    }

    fun saveExercicio(){
        val exercicio_nome = insira_nome_et.text.toString()
        val grupo_nome = grupos_spinner.selectedItem.toString()
        val intent = Intent()
        val aparelho = AparelhoModel(dbHelper.getGrupoByName(grupo_nome).IdGrupo, exercicio_nome)
        if(dbHelper.createAparelho(aparelho)!= -1) {
            Toast.makeText(this, getString(R.string.salvo_succ), Toast.LENGTH_SHORT).show()
            intent.putExtra("changed", 1)
            setResult(RESULT_OK, intent)
        }
        else{
            Toast.makeText(this, "Já existe um aparelho com esse nome!", Toast.LENGTH_SHORT).show()
            intent.putExtra("changed", 0)
            setResult(RESULT_CANCELED, intent)
        }
        finish()
    }
}