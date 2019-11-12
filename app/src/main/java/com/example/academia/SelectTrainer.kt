package com.example.academia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.academia.Database.DatabaseHelper
import kotlinx.android.synthetic.main.activity_selecttrainer.*

class SelectTrainer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecttrainer)
        //this.deleteDatabase("database.db")

        val dbHelper = DatabaseHelper(this)

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
            Log.d("selected", prof_list.selectedItem.toString())
            val intent = MainActivity.start(this, prof_list.selectedItem.toString())
            startActivity(intent)
        }
    }




}
