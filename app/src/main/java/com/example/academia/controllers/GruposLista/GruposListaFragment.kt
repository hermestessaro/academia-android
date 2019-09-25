package com.example.academia.controllers.GruposLista

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.academia.DatabaseHelper
import com.example.academia.R
import com.example.academia.controllers.EditGruposActivity
import com.example.academia.models.GrupoModel
import kotlinx.android.synthetic.main.aparelho_list_item.*
import kotlinx.android.synthetic.main.aparelho_list_item.view.*
import kotlinx.android.synthetic.main.fragment_grupos.*

class GruposListaFragment(val selectingExercises: Boolean) : Fragment() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var grupos: MutableList<GrupoModel>
    lateinit var data: HashMap<String, MutableList<String>>
    lateinit var adapter: AparelhosAdapter

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
        adapter = AparelhosAdapter(context!!, titleList, listData)
        groups_lv.setAdapter(adapter)
        if(selectingExercises){
            groups_lv.setOnChildClickListener{ parent, v, groupPosition, childPosition, id ->
                v.delete_img.setOnClickListener {
                    val name = v.expandedGrupoListItem.text.toString()
                    deleteAparelho(name)
                }
                //aqui vai mandar começar o popup
                Toast.makeText(context!!, "BAUSGURIClicked: " + titleList[groupPosition] + " -> " + listData[titleList[groupPosition]]!!.get(childPosition), Toast.LENGTH_SHORT).show()
                false
            }
        }
        else {
            groups_lv.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                v.delete_img.setOnClickListener {
                    val name = v.expandedGrupoListItem.text.toString()
                    deleteAparelho(name)
                }
                //Toast.makeText(context!!, "Clicked: " + titleList[groupPosition] + " -> " + listData[titleList[groupPosition]]!!.get(childPosition), Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    fun deleteAparelho(nomeAparelho : String){
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Atenção!")
        builder.setMessage("Tem certeza que deseja deletar o exercício $nomeAparelho?")
        builder.setPositiveButton("Sim"){dialog, which ->
            if(dbHelper.deleteAparelho(nomeAparelho)>0){
                Toast.makeText(context!!, "Deletado com sucesso", Toast.LENGTH_LONG).show()
                setUpList()
            }
            else { Toast.makeText(context!!, "Erro ao deletar", Toast.LENGTH_LONG).show() }
        }
        builder.setNegativeButton("Não"){_,_ ->
            Toast.makeText(context!!, "Cancelado", Toast.LENGTH_LONG).show()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }


}