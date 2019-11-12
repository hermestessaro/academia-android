package com.example.academia.Database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.academia.models.ExercicioModel

class DHExercicio(val db: SQLiteDatabase) {

    private val TABLE_EXERCICIO = "Exercicio"

    //Common column names
    private val ID = "id"
    private val KEY_NAME = "Nome"
    private val KEY_ACTIVE = "IndicadorAtivo"
    private val KEY_DATA_INC = "DataInclusao"
    private val KEY_DATA_ULT = "DataHoraUltimaAtu"

    //Exercicio column names
    private val ID_EXERCICIO = "IdExercicio"
    private val KEY_SERIES = "Series"
    private val KEY_REPS = "Repeticoes"
    private val KEY_PESO = "Peso"

    private val ID_APARELHO = "IdAparelho"
    private val ID_TREINO = "IdTreino"
    private val ID_ALUNO = "IdAluno"


    fun getExerciciosByIdTreino(id: Int, idAluno: Int): MutableList<ExercicioModel>{
        val selectQuery = "SELECT * FROM $TABLE_EXERCICIO WHERE $ID_TREINO = $id AND $ID_ALUNO = $idAluno"
        val exercicios: MutableList<ExercicioModel> = ArrayList()
        val c: Cursor = db.rawQuery(selectQuery, null)
        if(c.moveToFirst()) {
            do {
                val exercicio = ExercicioModel(c.getInt(c.getColumnIndex(ID_TREINO)),
                    c.getInt(c.getColumnIndex(ID_ALUNO)),
                    c.getString(c.getColumnIndex(ID_APARELHO)),
                    //c.getString(c.getColumnIndex(KEY_NAME)),
                    c.getInt(c.getColumnIndex(KEY_SERIES)),
                    c.getInt(c.getColumnIndex(KEY_REPS)),
                    c.getInt(c.getColumnIndex(KEY_PESO))
                )
                exercicios.add(exercicio)

            } while (c.moveToNext())
        }

        return exercicios
    }

    fun saveExercicio(exercicio: ExercicioModel){
        val values = ContentValues()
        values.put(ID_TREINO, exercicio.idTreino)
        values.put(ID_ALUNO, exercicio.idAluno)
        values.put(ID_APARELHO, exercicio.nomeAparelho)
        values.put(KEY_SERIES, exercicio.series)
        values.put(KEY_REPS, exercicio.repeticoes)
        values.put(KEY_PESO, exercicio.peso)
        val row = db.insert(TABLE_EXERCICIO, null, values)
        Log.d("rowExercicio", row.toString())
    }
}