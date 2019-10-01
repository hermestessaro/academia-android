package com.example.academia.controllers.NewTreino

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.academia.DatabaseHelper
import com.example.academia.R
import com.example.academia.controllers.VisualizeTreino.VisualizeTreinoFragment
import com.example.academia.models.ExercicioModel
import kotlinx.android.synthetic.main.fragment_new_treino_third_screen.*
import org.w3c.dom.Text

class AddExercicioDialogFragment : DialogFragment() {

    lateinit var nomeExercicio: TextView
    lateinit var dbHelper: DatabaseHelper
    var nome: String? = ""
    var idTreino: Int? = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        nome = arguments?.getString("nome")
        idTreino = arguments?.getInt("idTreino")
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

        val exercicio = ExercicioModel(idTreino!!, nome!!, s, r, c)
        dbHelper.saveExercicio(exercicio)
        val main = activity as NewTreino
        main.supportFragmentManager.beginTransaction().replace(R.id.content_frame, VisualizeTreinoFragment(true, idTreino!!)).addToBackStack(null).commit()
        dismiss()
    }

    fun newInstance(content: String, id: Int): AddExercicioDialogFragment {
        val frag = AddExercicioDialogFragment()

        val args = Bundle()
        args.putString("nome", content)
        args.putInt("idTreino", id)
        frag.arguments = args

        return frag
    }
}