package com.example.academia.controllers.GruposLista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.academia.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_grupos.*

class GruposListaFragment : Fragment() {

    //for tests only
    val data: HashMap<String, List<String>>
        get(){
            val listData = HashMap<String, List<String>>()

            val peitoExercicios = ArrayList<String>()
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

            listData["Peito"] = peitoExercicios
            listData["Costas"] = costasExercicios
            listData["Triceps"] = tricepsExercicios
            listData["Biceps"] = bicepsExercicios
            listData["Ombros"] = ombrosExercicios
            listData["Pernas"] = pernasExercicios

            return listData
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_grupos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(groups_lv != null){

            //TEST
            val listData = data

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