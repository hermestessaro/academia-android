package com.example.academia
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.academia.controllers.AlunoDetail.AlunoDetailFragment
import com.example.academia.controllers.AlunosLista.AlunosListaFragment
import com.example.academia.controllers.GruposLista.GruposListaFragment
import com.example.academia.models.AlunoModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.toolbar

class MainActivity : AppCompatActivity(){


    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar_layout: androidx.appcompat.widget.Toolbar
    val manager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        //val profName = intent.getStringExtra("PROF")
        val profName = "Prof. Renato"
        //TODO: SYNC
        //this.deleteDatabase("database.db")
        val dbHelper = DatabaseHelper(this)

        dbHelper.createGrupo("Peito")
        dbHelper.createGrupo("Costas")
        dbHelper.createGrupo("Triceps")
        dbHelper.createGrupo("Biceps")
        dbHelper.createGrupo("Ombros")
        dbHelper.createGrupo("Pernas")
        dbHelper.createGrupo("Abdomen")

        val grupos = dbHelper.getAllGrupos()
        for(item in grupos){
            Log.d("nomesgrupos", item.nome+item.id.toString())
        }
        /*dbHelper.createAparelho("Supino", "Peito")
        dbHelper.createAparelho("Puxada Frontal", "Costas")
        dbHelper.createAparelho("Corda", "Triceps")
        dbHelper.createAparelho("Barra reta", "Biceps")
        dbHelper.createAparelho("Desenvolvimento", "Ombros")
        dbHelper.createAparelho("Leg Press", "Pernas")
        dbHelper.createAparelho("Obliquo", "Abdomen")*/




    }

    fun initView(){
        toolbar_layout = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.navDrawer)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar_layout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val alunosFragment = AlunosListaFragment()
        val gruposFragment = GruposListaFragment()
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.content_frame, alunosFragment)
        transaction.addToBackStack(null)
        transaction.commit()

        navView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.nav_item_one -> changesFragment(alunosFragment)
                R.id.nav_item_two -> startActivity(Intent(this, NewAluno::class.java))
                R.id.nav_item_three -> Toast.makeText(this, "treino", Toast.LENGTH_LONG).show()
                R.id.nav_item_four -> Toast.makeText(this, "profs", Toast.LENGTH_LONG).show()
                R.id.nav_item_five -> Toast.makeText(this, "aparelhos", Toast.LENGTH_LONG).show()
                R.id.nav_item_six -> changesFragment(gruposFragment)
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    fun changesFragment(fragment: Fragment){
        manager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit()
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