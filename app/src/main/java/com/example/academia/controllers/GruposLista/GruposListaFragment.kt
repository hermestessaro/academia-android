package com.example.academia.controllers.GruposLista

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.academia.DatabaseHelper
import com.example.academia.R
import com.example.academia.controllers.EditGruposActivity
import com.example.academia.models.GrupoModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_grupos.*

class GruposListaFragment : Fragment() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var grupos: MutableList<GrupoModel>
    lateinit var data: HashMap<String, MutableList<String>>
    lateinit var adapter: ExerciciosAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = DatabaseHelper(activity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = createList()
        //adapter.notifyDataSetChanged()
    }

    fun createList(): HashMap<String, MutableList<String>> {
        //for tests only
        grupos = dbHelper.getAllGrupos()
        val listData = HashMap<String, MutableList<String>>()

        for(item in grupos){
            val aparelhosList = dbHelper.getAparelhosByGrupo(item.nome)
            val aparelhosListNames: MutableList<String> = ArrayList()
            for(aparelho in aparelhosList){
                aparelhosListNames.add(aparelho.nome)
            }
            Log.d("names", item.nome)
            listData[item.nome] = aparelhosListNames
        }

        return listData

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_grupos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(groups_lv != null){
            setUpList()
        }
        editar_grupos.setOnClickListener {
            val intent = Intent(context, EditGruposActivity::class.java)
            this.startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(1 == requestCode){
                if(Activity.RESULT_OK == resultCode){
                    setUpList()
                }
        }
    }

    fun setUpList(){
        val listData = createList()

        val titleList = ArrayList(listData.keys)
        adapter = ExerciciosAdapter(context!!, titleList, listData)
        groups_lv.setAdapter(adapter)
        groups_lv.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            Toast.makeText(context!!, "Clicked: " + titleList[groupPosition] + " -> " + listData[titleList[groupPosition]]!!.get(childPosition), Toast.LENGTH_SHORT).show()
            false
        }
    }


}