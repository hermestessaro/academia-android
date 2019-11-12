package com.example.academia.Database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.academia.models.TreinoModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DHTreino(val db: SQLiteDatabase) {

    private val TABLE_TREINO = "Treino"
    private val ID = "id"
    private val KEY_NAME = "Nome"
    private val KEY_ACTIVE = "IndicadorAtivo"
    private val KEY_DATA_INC = "DataInclusao"
    private val KEY_DATA_ULT = "DataHoraUltimaAtu"
    private val ID_ALUNO = "IdAluno"
    private val ID_PROF = "IdProfessor"
    private val ID_TREINO = "IdTreino"
    private val KEY_OBJETIVOS = "Objetivos"
    private val KEY_TIPO = "Identificador"

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun getTreinosByIdAluno(idAluno: Int) : MutableList<TreinoModel>{
        val selectQuery = "SELECT * FROM $TABLE_TREINO WHERE $ID_ALUNO = $idAluno AND $KEY_ACTIVE = 1;"
        val treinos: MutableList<TreinoModel> = ArrayList()
        val c: Cursor = db.rawQuery(selectQuery, null)
        if(c.moveToFirst()) {
            do {
                val treino = TreinoModel(c.getInt(c.getColumnIndex(ID_TREINO)),
                    c.getInt(c.getColumnIndex(ID_PROF)),
                    c.getInt(c.getColumnIndex(ID_ALUNO)),
                    c.getString(c.getColumnIndex(KEY_NAME)),
                    c.getString(c.getColumnIndex(KEY_TIPO))
                )
                treinos.add(treino)

            } while (c.moveToNext())
        }

        return treinos
    }

    fun getTreinoById(idTreino: Int, idAluno: Int) : TreinoModel {
        val selectQuery = "SELECT * FROM $TABLE_TREINO WHERE $ID_TREINO = $idTreino AND $ID_ALUNO = $idAluno AND $KEY_ACTIVE = 1;"
        val c: Cursor = db.rawQuery(selectQuery, null)
        c.moveToFirst()
        val treino = TreinoModel(c.getInt(c.getColumnIndex(ID_TREINO)),
            c.getInt(c.getColumnIndex(ID_PROF)),
            c.getInt(c.getColumnIndex(ID_ALUNO)),
            c.getString(c.getColumnIndex(KEY_NAME)),
            c.getString(c.getColumnIndex(KEY_TIPO))
        )

        return treino
    }

    fun saveTreino(treino: TreinoModel){
        val values = ContentValues()
        values.put(ID_TREINO, treino.idTreino)
        values.put(ID_PROF, treino.idProf)
        values.put(ID_ALUNO, treino.idAluno)
        values.put(KEY_TIPO, treino.tipo)
        values.put(KEY_NAME, treino.nome)
        values.put(KEY_OBJETIVOS, "")
        values.put(KEY_DATA_INC, LocalDate.now().toString())
        values.put(KEY_DATA_ULT, LocalDateTime.now().format(formatter).toString())
        values.put(KEY_ACTIVE, 1)
        val row = db.insert(TABLE_TREINO, null, values)
        Log.d("rowTreino", row.toString())
    }


    fun deleteTreino(idAluno: Int, idTreino: Int) {
        /*val whereClause = "$ID_ALUNO=? AND $ID_TREINO=?"
        db.delete(TABLE_TREINO, whereClause, arrayOf(idAluno.toString(), idTreino.toString()))
        db.delete(TABLE_EXERCICIO, whereClause, arrayOf(idAluno.toString(), idTreino.toString()))*/
        val updateQuery = "UPDATE $TABLE_TREINO SET $KEY_ACTIVE = 0 WHERE $ID_ALUNO = $idAluno AND $ID_TREINO = $idTreino;"
        db.rawQuery(updateQuery, null)
    }

    fun updateTreino(treino: TreinoModel){
        val values = ContentValues()
        values.put(ID_TREINO, treino.idTreino)
        values.put(ID_PROF, treino.idProf)
        values.put(ID_ALUNO, treino.idAluno)
        values.put(KEY_TIPO, treino.tipo)
        values.put(KEY_NAME, treino.nome)
        val whereClause = "$ID_ALUNO=? AND $ID_TREINO=?"
        db.update(TABLE_TREINO, values, whereClause, arrayOf(treino.idAluno.toString(), treino.idTreino.toString()))

    }
}