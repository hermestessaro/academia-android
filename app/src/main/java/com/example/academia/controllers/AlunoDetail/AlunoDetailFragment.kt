package com.example.academia.controllers.AlunoDetail

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.academia.Database.DatabaseHelper
import com.example.academia.MainActivity
import com.example.academia.R
import com.example.academia.controllers.NewTreino.NewTreino
import com.example.academia.models.AlunoModel
import com.example.academia.models.TreinoModel
import kotlinx.android.synthetic.main.detail_header.view.*
import kotlinx.android.synthetic.main.fragment_aluno_detail.*
import kotlinx.android.synthetic.main.week_representation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AlunoDetailFragment() : Fragment() {

    lateinit var aluno: AlunoModel
    var idAluno = -1
    lateinit var dbHelper: DatabaseHelper
    lateinit var data: HashMap<String, MutableList<String>>
    lateinit var adapter: TreinosAdapter
    var lastIndex = -1
    lateinit var profName: String


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dbHelper = DatabaseHelper(activity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            this.aluno = arguments!!.getParcelable("alunoClicked")!!
        }
        Log.d("datainclusao", this.aluno.DataInclusao)
        idAluno = dbHelper.getIdAlunoByName(this.aluno.Nome)
    }

    override fun onResume() {
        setUpView()
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //container?.removeAllViews()
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_aluno_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val nome = view.findViewById<TextView>(R.id.nome_aluno)
        //nome.text = aluno.nome
        setUpView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_aluno_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.excluir_aluno_detail -> {
                deleteAluno()
                return true
            }
            R.id.editar_aluno_detail -> {
                editAluno()
                return true
            }
            R.id.calendario_aluno_detail -> {
                mostrarCalendario()
                return true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    fun setUpView(){
        setUpList()
        val dropdownObs = dropdown_obs
        //val dropdownTreino = dropdown_treino_esp
        //val dropdownLesoes = dropdown_lesoes

        //idAluno = dbHelper.getIdAlunoByName(aluno.nome)
        /*val disp = dbHelper.getDisp(idAluno)

        if(disp.seg) { day_1.setBackgroundColor(resources.getColor(R.color.colorGreen)) }
        if(disp.ter) { day_2.setBackgroundColor(resources.getColor(R.color.colorGreen)) }
        if(disp.qua) { day_3.setBackgroundColor(resources.getColor(R.color.colorGreen)) }
        if(disp.qui) { day_4.setBackgroundColor(resources.getColor(R.color.colorGreen)) }
        if(disp.sex) { day_5.setBackgroundColor(resources.getColor(R.color.colorGreen)) }
        if(disp.sab) { day_6.setBackgroundColor(resources.getColor(R.color.colorGreen)) }
        if(disp.dom) { day_7.setBackgroundColor(resources.getColor(R.color.colorGreen)) }*/

        profName = dbHelper.getProfessorById(aluno.IdProf)!!.Nome

        container.nome_aluno.text = aluno.Nome

        val date = LocalDate.parse(aluno.DataNascimento) //data em formato yyyy-MM-dd
        val date_text: String = date.dayOfMonth.toString() +"/"+ date.monthValue.toString() +"/"+ date.year.toString()
        container.data_nascimento.text = date_text

        container.prof.text = profName
        dropdownObs.setContentText(setObservations())
        //dropdownLesoes.setContentText(aluno.lesoes)

    }

    fun setObservations(): String{
        var final_text = "Observações: ${aluno.Observacoes}\nLesões: ${aluno.Lesoes}\n"
        val auxDorAtividades = "Dor no peito por atividades: "
        val auxDorUltimoMes = "Dor no peito no último mês: "
        val auxPerdaConsciencia = "Já perdeu a consciência: "
        val auxProblemaArticular = "Possui problema ósseo ou articular: "
        val auxTabagista = "Tabagista: "
        val auxDiabetico = "Diabético: "
        val auxCardiaco = "Histórico de ataque cardíaco: "

        if(aluno.IndicadorDorPeitoAtividadesFisicas) auxDorAtividades + "Sim\n" else auxDorAtividades + "Não\n"
        if(aluno.IndicadorDorPeitoUltimoMes) auxDorUltimoMes + "Sim\n" else auxDorUltimoMes + "Não\n"
        if(aluno.IndicadorPerdaConscienciaTontura) auxPerdaConsciencia + "Sim\n" else auxPerdaConsciencia + "Não\n"
        if(aluno.IndicadorProblemaArticular) auxProblemaArticular + "Sim\n" else auxProblemaArticular + "Não\n"
        if(aluno.IndicadorTabagista) auxTabagista + "Sim\n" else auxTabagista + "Não\n"
        if(aluno.IndicadorDiabetico) auxDiabetico + "Sim\n" else auxDiabetico + "Não\n"
        if(aluno.IndicadorFamiliarAtaqueCardiaco) auxCardiaco + "Sim\n" else auxCardiaco + "Não\n"

        final_text + auxDorAtividades + auxDorUltimoMes + auxPerdaConsciencia + auxProblemaArticular + auxTabagista + auxDiabetico + auxCardiaco

        return final_text
    }

    fun createList(): HashMap<String, MutableList<TreinoModel>> {
        val listData = HashMap<String, MutableList<TreinoModel>>()
        val data = dbHelper.getTreinosByIdAluno(idAluno)

        //mesmo fora de testes, isso vai continuar aqui
        data.add(TreinoModel(-1, 0, idAluno, "Criar rotina", ""))
        lastIndex = data.lastIndex


        listData["Ver treinos"] = data

        return listData

    }

    fun setUpList(){
        val listData = createList()

        val titleList = ArrayList(listData.keys)
        Log.d("list_data_size", listData["Ver treinos"]!!.size.toString())
        adapter = TreinosAdapter(context!!, titleList, listData)
        treinos_lv.setAdapter(adapter)
        treinos_lv.setOnChildClickListener{ parent, v, groupPosition, childPosition, id ->
            var new = false
            if(childPosition == lastIndex){
                new = true
            }
            val aux = adapter.getChild(groupPosition, childPosition) as TreinoModel
            Log.d("id do treino", aux.idTreino.toString())
            lateinit var intent: Intent
            if(new){
                intent = NewTreino.start(context!!, new, listData["Ver treinos"]!!.size, idAluno)
            }
            else{
                intent = NewTreino.start(context!!, new, aux.idTreino, idAluno)
            }
            startActivity(intent)
            //Toast.makeText(context!!, "Clicked: " + titleList[groupPosition] + " -> " + listData[titleList[groupPosition]]!!.get(childPosition), Toast.LENGTH_SHORT).show()
            false
        }
    }


    fun deleteAluno(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Excluir aluno")
        builder.setMessage("Tem certeza que gostaria de excluir este aluno?")
        builder.setPositiveButton("Sim"){dialog, which ->
            dbHelper.deleteAluno(idAluno)
            //val main = activity as MainActivity
            //main.supportFragmentManager.popBackStack()
            val intent = Intent(context, MainActivity::class.java).putExtra("profName", profName)
            startActivity(intent)
        }
        builder.setNegativeButton("Não"){_, _ ->}

        builder.create().show()



    }

    fun editAluno(){
        Toast.makeText(context, "editar aluno", Toast.LENGTH_LONG).show()
    }

    fun mostrarCalendario(){
        Toast.makeText(context, "calendario", Toast.LENGTH_LONG).show()
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