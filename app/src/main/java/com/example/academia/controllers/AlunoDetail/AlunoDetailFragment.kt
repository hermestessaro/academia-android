package com.example.academia.controllers.AlunoDetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.academia.DatabaseHelper
import com.example.academia.R
import com.example.academia.controllers.NewTreino.NewTreino
import com.example.academia.controllers.VisualizeTreino.ExerciciosAdapter
import com.example.academia.models.AlunoModel
import kotlinx.android.synthetic.main.detail_header.view.*
import kotlinx.android.synthetic.main.fragment_aluno_detail.*
import kotlinx.android.synthetic.main.fragment_aluno_detail.view.*
import kotlinx.android.synthetic.main.week_representation.*
import org.w3c.dom.Text

class AlunoDetailFragment() : Fragment() {

    lateinit var aluno: AlunoModel
    lateinit var dbHelper: DatabaseHelper
    lateinit var data: HashMap<String, MutableList<String>>
    lateinit var adapter: TreinosAdapter
    var lastIndex = -1


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = DatabaseHelper(activity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            this.aluno = arguments!!.getParcelable("alunoClicked")!!
        }
        Log.d("nome", this.aluno.nome)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_aluno_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val nome = view.findViewById<TextView>(R.id.nome_aluno)
        //nome.text = aluno.nome
        setUpView()

    }

    fun setUpView(){
        setUpList()
        val dropdownObs = dropdown_obs
        val dropdownTreino = dropdown_treino_esp
        val dropdownLesoes = dropdown_lesoes

        val disp = dbHelper.getDisp(dbHelper.getIdAlunoByName(aluno.nome))

        if(disp.seg) { day_1.setBackgroundColor(resources.getColor(R.color.colorGreen)) }
        if(disp.ter) { day_2.setBackgroundColor(resources.getColor(R.color.colorGreen)) }
        if(disp.qua) { day_3.setBackgroundColor(resources.getColor(R.color.colorGreen)) }
        if(disp.qui) { day_4.setBackgroundColor(resources.getColor(R.color.colorGreen)) }
        if(disp.sex) { day_5.setBackgroundColor(resources.getColor(R.color.colorGreen)) }
        if(disp.sab) { day_6.setBackgroundColor(resources.getColor(R.color.colorGreen)) }
        if(disp.dom) { day_7.setBackgroundColor(resources.getColor(R.color.colorGreen)) }


        container.nome_aluno.text = aluno.nome
        container.data_nascimento.text = aluno.dataNascimento
        container.prof.text = aluno.prof
        dropdownObs.setContentText(aluno.observacoes)
        dropdownLesoes.setContentText(aluno.lesoes)

    }

    fun createList(): HashMap<String, MutableList<String>> {
        val listData = HashMap<String, MutableList<String>>()

        val data = mutableListOf<String>("Peitos e triceps", "Costas e biceps", "Pernas e biceps")

        //mesmo fora de testes, isso vai continuar aqui
        data.add("Criar rotina")
        lastIndex = data.lastIndex


        listData["Ver treinos"] = data

        return listData

    }

    fun setUpList(){
        val listData = createList()

        val titleList = ArrayList(listData.keys)
        adapter = TreinosAdapter(context!!, titleList, listData)
        treinos_lv.setAdapter(adapter)
        treinos_lv.setOnChildClickListener{ parent, v, groupPosition, childPosition, id ->
            var new = false
            if(childPosition == lastIndex){
                new = true
            }
            val intent = NewTreino.start(context!!, new)
            startActivity(intent)
            Toast.makeText(context!!, "Clicked: " + titleList[groupPosition] + " -> " + listData[titleList[groupPosition]]!!.get(childPosition), Toast.LENGTH_SHORT).show()
            false
        }
    }



    companion object {
        fun newInstance(alunoClicked: AlunoModel): AlunoDetailFragment {
            val frag = AlunoDetailFragment()
            val args = Bundle()
            args.putParcelable("alunoClicked", alunoClicked)
            frag.arguments = args
            return frag
        }

    }
}