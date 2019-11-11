package com.example.academia.controllers.NewTreino

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.academia.DatabaseHelper
import com.example.academia.R
import com.example.academia.models.ExercicioModel

class AddExercicioDialogFragment : DialogFragment() {

    lateinit var nomeExercicio: TextView
    lateinit var dbHelper: DatabaseHelper
    var nome: String? = ""
    var idTreino: Int? = -1
    var idAluno: Int? = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        nome = arguments?.getString("nome")
        idTreino = arguments?.getInt("idTreino")
        idAluno = arguments?.getInt("idAluno")
        val style = DialogFragment.STYLE_NO_FRAME
        val theme = R.style.ExercicioDialogFrag
        setStyle(style, theme)
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        dbHelper = DatabaseHelper(activity)
        super.onAttach(context)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_new_treino_third_screen, container, false)
        val nomeExercicio = view.findViewById<TextView>(R.id.nome_exercicio)
        nomeExercicio.text=nome
        val seriesET = view.findViewById<EditText>(R.id.series_et)
        val repsET = view.findViewById<EditText>(R.id.reps_et)
        val cargaET = view.findViewById<EditText>(R.id.carga_et)
        val addSeries = view.findViewById<ImageView>(R.id.adding_button_series)
        val subSeries = view.findViewById<ImageView>(R.id.sub_button_series)
        val addReps = view.findViewById<ImageView>(R.id.adding_button_reps)
        val subReps = view.findViewById<ImageView>(R.id.sub_button_reps)
        val addCarga = view.findViewById<ImageView>(R.id.adding_button_carga)
        val subCarga = view.findViewById<ImageView>(R.id.sub_button_carga)
        val okButton = view.findViewById<Button>(R.id.ok)

        seriesET.setText("0")
        repsET.setText("0")
        cargaET.setText("0")


        addSeries.setOnClickListener {
            if(seriesET.text.isNullOrEmpty()){
                seriesET.setText("1")
            }
            else{
                var value = seriesET.text.toString().toInt()
                value += 1
                seriesET.setText(value.toString())
            }
        }

        subSeries.setOnClickListener {
            if((!seriesET.text.isNullOrEmpty())&&(seriesET.text.toString() != "0")){
                var value = seriesET.text.toString().toInt()
                value -= 1
                seriesET.setText(value.toString())
            }
        }

        addReps.setOnClickListener {
            if(repsET.text.isNullOrEmpty()){
                repsET.setText("1")
            }
            else{
                var value = repsET.text.toString().toInt()
                value += 1
                repsET.setText(value.toString())
            }
        }

        subReps.setOnClickListener {
            if((!repsET.text.isNullOrEmpty())&&(repsET.text.toString() != "0")){
                var value = repsET.text.toString().toInt()
                value -= 1
                repsET.setText(value.toString())
            }
        }

        addCarga.setOnClickListener {
            if(cargaET.text.isNullOrEmpty()){
                cargaET.setText("1")
            }
            else{
                var value = cargaET.text.toString().toInt()
                value += 1
                cargaET.setText(value.toString())
            }
        }

        subCarga.setOnClickListener {
            if((!cargaET.text.isNullOrEmpty())&&(cargaET.text.toString() != "0")){
                var value = cargaET.text.toString().toInt()
                value -= 1
                cargaET.setText(value.toString())
            }
        }

        okButton.setOnClickListener {
            if((seriesET.text.isNullOrEmpty()) || (repsET.text.isNullOrEmpty()) ||
                (cargaET.text.isNullOrEmpty())){
                Toast.makeText(context, "Por favor, preencha as informações!", Toast.LENGTH_LONG).show()
            }
            else{
                val series: Int = Integer.parseInt(seriesET.text.toString())
                val reps: Int = Integer.parseInt(repsET.text.toString())
                val carga: Int = Integer.parseInt(cargaET.text.toString())
                Toast.makeText(context, "usguri", Toast.LENGTH_LONG).show()
                saveExercicio(series, reps, carga)
            }


        }
        return view
    }

    fun saveExercicio(s: Int, r: Int, c: Int){

        val exercicio = ExercicioModel(idTreino!!, idAluno!!, nome!!, s, r, c)
        dbHelper.saveExercicio(exercicio)
        val main = activity as NewTreino
        main.supportFragmentManager.popBackStack()
        //main.supportFragmentManager.beginTransaction().replace(R.id.content_frame, CreateTreinoFragment(false, idTreino!!, idAluno)).addToBackStack(null).commit()
        dismiss()
    }

    fun newInstance(content: String, id: Int, aluno: Int?): AddExercicioDialogFragment {
        val frag = AddExercicioDialogFragment()

        val args = Bundle()
        args.putString("nome", content)
        args.putInt("idTreino", id)
        if(aluno != null) {
            args.putInt("idAluno", aluno)
        }
        frag.arguments = args

        return frag
    }
}