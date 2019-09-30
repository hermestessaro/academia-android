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
import com.example.academia.R
import kotlinx.android.synthetic.main.fragment_new_treino_third_screen.*
import org.w3c.dom.Text

class AddExercicioDialogFragment : DialogFragment() {

    lateinit var nomeExercicio: TextView
    var nome: String? = ""
//    lateinit var seriesET: EditText
//    lateinit var repsET: EditText
//    lateinit var cargaET: EditText
//    lateinit var addSeries: ImageView
//    lateinit var subSeries: ImageView
//    lateinit var addReps: ImageView
//    lateinit var subReps: ImageView
//    lateinit var addCarga: ImageView
//    lateinit var subCarga: ImageView
//    lateinit var okButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        nome = arguments?.getString("content")
        val style = DialogFragment.STYLE_NO_FRAME
        val theme = R.style.ExercicioDialogFrag
        setStyle(style, theme)
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_new_treino_third_screen, container, false)
        val nomeExercicio = view.findViewById<TextView>(R.id.nome_exercicio)
        nomeExercicio.text="Corote azul de morango"
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
            val series_text: String? = series_et.text.toString()
            val reps_text: String? = reps_et.text.toString()
            val carga_text: String? = carga_et.text.toString()
            if(series_text!= null){val series: Int = Integer.parseInt(seriesET.text.toString())}
            if(reps_text!=null){val reps: Int = Integer.parseInt(repsET.text.toString())}
            if(carga_text!=null){ val carga: Int = Integer.parseInt(cargaET.text.toString())}

            Toast.makeText(context, "usguri", Toast.LENGTH_LONG).show()
            dismiss()
        }
        return view
    }

    fun newInstance(content: String): AddExercicioDialogFragment {
        val frag = AddExercicioDialogFragment()

        val args = Bundle()
        args.putString("nome", content)
        frag.arguments = args

        return frag
    }
}