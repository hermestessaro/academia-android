package com.example.academia

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.academia.models.ProfessorModel
import kotlinx.android.synthetic.main.activity_selecttrainer.*

class SelectTrainer() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecttrainer)
        //this.deleteDatabase("database.db")

        val dbHelper = DatabaseHelper(this)

        dbHelper.createGrupo("Peito")
        dbHelper.createGrupo("Costas")
        dbHelper.createGrupo("Triceps")
        dbHelper.createGrupo("Biceps")
        dbHelper.createGrupo("Ombros")
        dbHelper.createGrupo("Pernas")
        dbHelper.createGrupo("Abdomen")

        /*val prof1 = ProfessorModel(0, "Renato", "renato@gremio.com", "1234", "1414141", "121212", "Sim")
        val prof2 = ProfessorModel(1, "Xavier", "xavier@xmen.com", "4321","1414141", "121212", "Sim")
        dbHelper.createProfessor(prof1)
        dbHelper.createProfessor(prof2)*/

        val professors = ArrayList<String>()
        for(item in dbHelper.getAllProfessors()){
            professors.add(item.Nome)
        }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, professors)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prof_list.adapter = adapter

        button_prof.setOnClickListener {
            val intent = MainActivity.start(this, prof_list.selectedItem.toString())
            startActivity(intent)
        }
    }




}
