package com.example.academia.controllers.GruposLista

import android.content.Context
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
import com.example.academia.models.GrupoModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_grupos.*

class GruposListaFragment : Fragment() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var grupos: MutableList<GrupoModel>
    lateinit var data: HashMap<String, MutableList<String>>


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = DatabaseHelper(activity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        data = createList()
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


        /*val peitoExercicios = ArrayList<String>()
        peitoExercicios.add("Supino vertical")
        peitoExercicios.add("Crucifixo inclinado")
        peitoExercicios.add("Voador")
        peitoExercicios.add("Supino horizontal")
        peitoExercicios.add("Crossover")

        val costasExercicios = ArrayList<String>()
        costasExercicios.add("Puxada frontal")
        costasExercicios.add("Remada")
        costasExercicios.add("Serrote")
        costasExercicios.add("Pull Down")

        val tricepsExercicios = ArrayList<String>()
        tricepsExercicios.add("Testa com barra")
        tricepsExercicios.add("Roldana com corda")
        tricepsExercicios.add("Francesa")

        val bicepsExercicios = ArrayList<String>()
        bicepsExercicios.add("Rosca Direta")
        bicepsExercicios.add("Rosca Alternada")
        bicepsExercicios.add("Concentrado")

        val ombrosExercicios = ArrayList<String>()
        ombrosExercicios.add("Elevação Lateral")
        ombrosExercicios.add("Elevação Frontal")
        ombrosExercicios.add("Desenvolvimento")
        ombrosExercicios.add("Remada Alta")

        val pernasExercicios = ArrayList<String>()
        pernasExercicios.add("Agachamento na barra")
        pernasExercicios.add("Leg Press")
        pernasExercicios.add("Flexora")
        pernasExercicios.add("Extensora")
        pernasExercicios.add("Adutora")
        pernasExercicios.add("Abdutora")

        listData[grupos.component1().nome] = peitoExercicios
        listData[grupos.component2().nome] = costasExercicios
        listData[grupos.component3().nome] = tricepsExercicios
        listData[grupos.component4().nome] = bicepsExercicios
        listData[grupos.component5().nome] = ombrosExercicios*/

        return listData

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_grupos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(groups_lv != null){

            //TEST
            val listData = createList()

            val titleList = ArrayList(listData.keys)
            val adapter = ExerciciosAdapter(context!!, titleList, listData)
            groups_lv.setAdapter(adapter)
            groups_lv.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                Toast.makeText(context!!, "Clicked: " + titleList[groupPosition] + " -> " + listData[titleList[groupPosition]]!!.get(childPosition), Toast.LENGTH_SHORT).show()
                false
            }

        }
    }


}