package com.example.academia
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.academia.Database.DatabaseHelper
import com.example.academia.WebService.SyncHelper
import com.example.academia.controllers.AlunosLista.AllAlunosListaFragment
import com.example.academia.controllers.AlunosLista.MyAlunosListaFragment
import com.example.academia.controllers.GruposLista.GruposListaFragment
import com.example.academia.models.AlunoModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.detail_header.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {


    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar_layout: androidx.appcompat.widget.Toolbar
    private lateinit var pagerAdapter: PagerAdapter
    val manager = supportFragmentManager
    lateinit var profName: String
    lateinit var dbHelper: DatabaseHelper
    lateinit var syncHelper: SyncHelper
    lateinit var last_sync: LocalDateTime
    private var PRIVATE_MODE = 0
    private val PREF_FILE = "prefFile"
    private val PREF_NAME = "profName"
    private val PREF_DATE = "last_sync"



    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref: SharedPreferences = getSharedPreferences(PREF_FILE, PRIVATE_MODE)
        val aux = sharedPref.getString(PREF_DATE,"0000-00-00T00:00:00")

        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        last_sync = LocalDateTime.parse(aux, dateTimeFormatter)

        Log.d("last_sync", last_sync.toString())

        val data_inc = LocalDateTime.parse("2019-11-19 23:57:11", dateTimeFormatter)
        if(last_sync.isBefore(data_inc)){
                Log.d("data_format", "true")
        }
        Log.d("data_inc", data_inc.toString())
        dbHelper = DatabaseHelper(applicationContext)
        syncHelper = SyncHelper(dbHelper)

        profName = intent.getStringExtra("profName")!!
        initView()
        Log.d("profname", profName)




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sincronizar){
            //variables to check if there's internet connection
            val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var result = false
            connectivityManager.run {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                    result = when{
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
            if(result){
                sync_progress.visibility = View.VISIBLE
                GlobalScope.async {
                    sync()
                    sync_progress.visibility = View.GONE
                }
            }
            else{
                Toast.makeText(applicationContext, "Não foi detectada conexão com a internet", Toast.LENGTH_LONG).show()
            }



            /*val alunos_local = dbHelper.getAllAlunos()
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

            alunos_local?.forEach {
                Log.d("SYNCalunoslocal", it.DataInclusao)
                if(last_sync.isBefore(LocalDateTime.parse(it.DataInclusao, dateTimeFormatter))){
                    Log.d("SYNC", "entrou if")
                }
                else{
                    Log.d("SYNC", "if passed")
                    if(last_sync.isBefore(LocalDateTime.parse(it.DataHoraUltimaAtu, dateTimeFormatter))){
                        Log.d("SYNC", "entrou segundo if")
                    }
                }
            }*/

            val sharedPref: SharedPreferences = getSharedPreferences(PREF_DATE, PRIVATE_MODE)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val editor = sharedPref.edit()
            editor.putBoolean(PREF_DATE, true)

            val date = LocalDateTime.now().format(formatter).toString()
            Log.d("date", date)
            editor.putString(PREF_DATE, date)
            editor.apply()

        }
        return super.onOptionsItemSelected(item)
    }

    fun initView(){
        toolbar_layout = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.navDrawer)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar_layout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        pagerAdapter = PagerAdapter(manager, 2, profName)
        view_pager.adapter = pagerAdapter


        val alunosFragment = MyAlunosListaFragment(profName)
        val gruposFragment = GruposListaFragment(false, -1, null)
        /*val transaction = manager.beginTransaction()
        transaction.replace(R.id.content_frame, alunosFragment)
        transaction.addToBackStack(null)
        transaction.commit()*/

        navView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.nav_item_one -> {
                    manager.popBackStack()
                    view_pager.visibility = View.VISIBLE
                }
                R.id.nav_item_two -> {
                    view_pager.visibility = View.GONE
                    val intent = NewAluno.start(applicationContext, profName)
                    startActivity(intent)
                }
                //R.id.nav_item_four -> Toast.makeText(this, "profs", Toast.LENGTH_LONG).show()
                //R.id.nav_item_five -> Toast.makeText(this, "aparelhos", Toast.LENGTH_LONG).show()
                R.id.nav_item_six -> {
                    view_pager.visibility = View.GONE
                    changesFragment(gruposFragment)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    fun changesFragment(fragment: Fragment){
        manager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit()
    }

    fun viewPagerDisappears(){
        view_pager.visibility = View.GONE
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else
        {
            //super.onBackPressed()
        }
    }

    suspend fun sync(){
        Log.d("SYNC", "blo")
        syncHelper.syncAll(last_sync)
    }




    companion object {
        fun start(context: Context, prof: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("profName", prof)
            return intent
        }
    }



}