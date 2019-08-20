package com.example.academia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_selecttrainer.*

class SelectTrainer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecttrainer)

        val adapter = ArrayAdapter.createFromResource(this, R.array.prof_list_test, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prof_list.adapter = adapter

        button_prof.setOnClickListener {
            val intent = MainActivity.start(this, prof_list.selectedItem.toString())
            startActivity(intent)
        }
    }




}
