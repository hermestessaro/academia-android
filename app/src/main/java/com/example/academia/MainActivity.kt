package com.example.academia
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.academia.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.toolbar

class MainActivity : AppCompatActivity(){

    lateinit var db: DatabaseHelper
    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar_layout: androidx.appcompat.widget.Toolbar
    private val DB_NAME = "database.db"
    private val DB_VERSION = 1

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

        db = DatabaseHelper(applicationContext, DB_NAME, null, DB_VERSION)

        //TODO: SYNC

        val profName = intent.getStringExtra("PROF")

    }

    fun initView(){
        toolbar_layout = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.navDrawer)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar_layout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.nav_item_one -> Toast.makeText(this, "alunos", Toast.LENGTH_LONG).show()
                R.id.nav_item_two -> Toast.makeText(this, "exercicios", Toast.LENGTH_LONG).show()
                R.id.nav_item_three -> Toast.makeText(this, "treino", Toast.LENGTH_LONG).show()
                R.id.nav_item_four -> Toast.makeText(this, "profs", Toast.LENGTH_LONG).show()
                R.id.nav_item_five -> Toast.makeText(this, "aparelhos", Toast.LENGTH_LONG).show()
            }
            
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else
        {
            super.onBackPressed()
        }
    }


    companion object {
        fun start(context: Context, prof: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("PROF", prof)
            return intent
        }
    }



}