package com.example.academia.Database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.academia.models.AlunoModel
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DHAluno(val db: SQLiteDatabase) {

    private val TABLE_ALUNO = "Aluno"
    //Common column names
    private val ID = "id"
    private val KEY_NAME = "Nome"
    private val KEY_ACTIVE = "IndicadorAtivo"
    private val KEY_DATA_INC = "DataInclusao"
    private val KEY_DATA_ULT = "DataHoraUltimaAtu"

    //Aluno column names
    private val ID_ALUNO = "IdAluno"
    private val ID_INCL = "IdUsuarioInclusao"
    private val KEY_NASCIMENTO = "DataNascimento"
    private val KEY_PROF = "Professor"
    private val KEY_IND_1 = "IndicadorDorPeitoAtividadeFisicas"
    private val KEY_IND_2 = "IndicadorDorPeitoUltimoMes"
    private val KEY_IND_3 = "IndicadorPerdaConscienciaTontura"
    private val KEY_IND_4 = "IndicadorProblemaArticular"
    private val KEY_IND_5 = "IndicadorTabagista"
    private val KEY_IND_6 = "IndicadorDiabetico"
    private val KEY_IND_7 = "IndicadorFamiliarAtaqueCardiaco"
    private val KEY_LESOES = "Lesoes"
    private val KEY_OBS = "Observacoes"
    private val KEY_TREINO_ESP = "TreinoEspecifico"

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


    fun createAluno(aluno: AlunoModel){
    val values = ContentValues().apply {
        put(KEY_NAME, aluno.Nome)
        put(KEY_NASCIMENTO, aluno.DataNascimento)
        put(ID_INCL, aluno.IdProfessor)
        put(KEY_IND_1, aluno.IndicadorDorPeitoAtividadesFisicas)
        put(KEY_IND_2, aluno.IndicadorDorPeitoUltimoMes)
        put(KEY_IND_3, aluno.IndicadorPerdaConscienciaTontura)
        put(KEY_IND_4, aluno.IndicadorProblemaArticular)
        put(KEY_IND_5, aluno.IndicadorTabagista)
        put(KEY_IND_6, aluno.IndicadorDiabetico)
        put(KEY_IND_7, aluno.IndicadorFamiliarAtaqueCardiaco)
        put(KEY_LESOES, aluno.Lesoes)
        put(KEY_OBS, aluno.Observacoes)
        put(KEY_TREINO_ESP, aluno.TreinoEspecifico)
        put(KEY_DATA_INC, LocalDate.now().toString())
        put(KEY_DATA_ULT, LocalDateTime.now().format(formatter).toString())
        put(KEY_ACTIVE, 1)
    }


    db.insert(TABLE_ALUNO, null, values)

}

    fun getIdAlunoByName(name: String): Int {
        val selectQuery = "SELECT * FROM $TABLE_ALUNO WHERE $KEY_NAME = '$name' AND $KEY_ACTIVE = 1;"
        val c: Cursor = db.rawQuery(selectQuery, null)
        c.moveToLast()
        return c.getInt(c.getColumnIndex(ID_ALUNO))
    }

    fun getAllAlunos() : MutableList<AlunoModel> {
        Log.d("primeiro", "passou")
        val selectQuery : String = "SELECT * FROM $TABLE_ALUNO WHERE $KEY_ACTIVE = 1;"
        val c: Cursor = db.rawQuery(selectQuery, null)

        val alunosList : MutableList<AlunoModel> = ArrayList()
        if(c.moveToFirst()){
            do {
                val ind1 = c.getString(c.getColumnIndex(KEY_IND_1))
                val ind2 = c.getString(c.getColumnIndex(KEY_IND_2))
                val ind3 = c.getString(c.getColumnIndex(KEY_IND_3))
                val ind4 = c.getString(c.getColumnIndex(KEY_IND_4))
                val ind5 = c.getString(c.getColumnIndex(KEY_IND_5))
                val ind6 = c.getString(c.getColumnIndex(KEY_IND_6))
                val ind7 = c.getString(c.getColumnIndex(KEY_IND_7))
                val aluno = AlunoModel(c.getString(c.getColumnIndex(KEY_NAME)),
                    c.getString(c.getColumnIndex(KEY_NASCIMENTO)),
                    c.getInt(c.getColumnIndex(ID_INCL)),
                    ind1.toBoolean(),
                    ind2.toBoolean(),
                    ind3.toBoolean(),
                    ind4.toBoolean(),
                    ind5.toBoolean(),
                    ind6.toBoolean(),
                    ind7.toBoolean(),
                    c.getString(c.getColumnIndex(KEY_LESOES)),
                    c.getString(c.getColumnIndex(KEY_OBS)),
                    c.getString(c.getColumnIndex(KEY_TREINO_ESP)),
                    c.getString(c.getColumnIndex(KEY_DATA_INC)),
                    c.getString(c.getColumnIndex(KEY_DATA_ULT)),
                    c.getString(c.getColumnIndex(KEY_ACTIVE))!!.toBoolean())
                alunosList.add(aluno)



            }while(c.moveToNext())
        }

        return alunosList
    }

    fun getAlunoById(idAluno: Int): AlunoModel {
        val selectQuery = "SELECT * FROM $TABLE_ALUNO WHERE $ID_ALUNO = $idAluno AND $KEY_ACTIVE = 1;"
        val c: Cursor = db.rawQuery(selectQuery, null)
        c.moveToFirst()
        val ind1 = c.getString(c.getColumnIndex(KEY_IND_1))
        val ind2 = c.getString(c.getColumnIndex(KEY_IND_2))
        val ind3 = c.getString(c.getColumnIndex(KEY_IND_3))
        val ind4 = c.getString(c.getColumnIndex(KEY_IND_4))
        val ind5 = c.getString(c.getColumnIndex(KEY_IND_5))
        val ind6 = c.getString(c.getColumnIndex(KEY_IND_6))
        val ind7 = c.getString(c.getColumnIndex(KEY_IND_7))
        val aluno = AlunoModel(c.getString(c.getColumnIndex(KEY_NAME)),
            c.getString(c.getColumnIndex(KEY_NASCIMENTO)),
            c.getInt(c.getColumnIndex(ID_INCL)),
            ind1.toBoolean(),
            ind2.toBoolean(),
            ind3.toBoolean(),
            ind4.toBoolean(),
            ind5.toBoolean(),
            ind6.toBoolean(),
            ind7.toBoolean(),
            c.getString(c.getColumnIndex(KEY_LESOES)),
            c.getString(c.getColumnIndex(KEY_OBS)),
            c.getString(c.getColumnIndex(KEY_TREINO_ESP)),
            c.getString(c.getColumnIndex(KEY_DATA_INC)),
            c.getString(c.getColumnIndex(KEY_DATA_ULT)),
            c.getString(c.getColumnIndex(KEY_ACTIVE))!!.toBoolean())

        return aluno
    }

    fun getAlunosByIdProf(idProf: Int): MutableList<AlunoModel>{
        val alunos: MutableList<AlunoModel> = ArrayList()
        val selectQuery : String = "SELECT * FROM $TABLE_ALUNO WHERE $ID_INCL = $idProf AND $KEY_ACTIVE = 1"
        val c: Cursor = db.rawQuery(selectQuery, null)
        if(c.moveToFirst()) {
            do {
                val aluno = AlunoModel(c.getString(c.getColumnIndex(KEY_NAME)),
                    c.getString(c.getColumnIndex(KEY_NASCIMENTO)),
                    idProf,
                    c.getString(c.getColumnIndex(KEY_IND_1)).toBoolean(),
                    c.getString(c.getColumnIndex(KEY_IND_2)).toBoolean(),
                    c.getString(c.getColumnIndex(KEY_IND_3)).toBoolean(),
                    c.getString(c.getColumnIndex(KEY_IND_4)).toBoolean(),
                    c.getString(c.getColumnIndex(KEY_IND_5)).toBoolean(),
                    c.getString(c.getColumnIndex(KEY_IND_6)).toBoolean(),
                    c.getString(c.getColumnIndex(KEY_IND_7)).toBoolean(),
                    c.getString(c.getColumnIndex(KEY_LESOES)),
                    c.getString(c.getColumnIndex(KEY_OBS)),
                    c.getString(c.getColumnIndex(KEY_TREINO_ESP)),
                    c.getString(c.getColumnIndex(KEY_DATA_INC)),
                    c.getString(c.getColumnIndex(KEY_DATA_ULT)),
                    c.getString(c.getColumnIndex(KEY_ACTIVE))!!.toBoolean())
                alunos.add(aluno)

            } while (c.moveToNext())
        }

        return alunos
    }

    fun deleteAluno(idAluno: Int) {
        /* val whereClause = "$ID_ALUNO=?"
         db.delete(TABLE_ALUNO, whereClause, arrayOf(idAluno.toString()))
         db.delete(TABLE_DISP, whereClause, arrayOf(idAluno.toString()))
         db.delete(TABLE_TREINO, whereClause, arrayOf(idAluno.toString()))
         db.delete(TABLE_EXERCICIO, whereClause, arrayOf(idAluno.toString()))*/
        val updateQuery = "UPDATE $TABLE_ALUNO SET $KEY_ACTIVE = 0 WHERE $ID_ALUNO = $idAluno;"
        db.rawQuery(updateQuery, null)

    }

    fun updateAluno(aluno: AlunoModel, idAluno: Int){
        val values = ContentValues().apply {
            put(KEY_NAME, aluno.Nome)
            put(KEY_NASCIMENTO, aluno.DataNascimento)
            put(ID_INCL, aluno.IdProfessor)
            put(KEY_IND_1, aluno.IndicadorDorPeitoAtividadesFisicas)
            put(KEY_IND_2, aluno.IndicadorDorPeitoUltimoMes)
            put(KEY_IND_3, aluno.IndicadorPerdaConscienciaTontura)
            put(KEY_IND_4, aluno.IndicadorProblemaArticular)
            put(KEY_IND_5, aluno.IndicadorTabagista)
            put(KEY_IND_6, aluno.IndicadorDiabetico)
            put(KEY_IND_7, aluno.IndicadorFamiliarAtaqueCardiaco)
            put(KEY_LESOES, aluno.Lesoes)
            put(KEY_OBS, aluno.Observacoes)
            put(KEY_TREINO_ESP, aluno.TreinoEspecifico)
            put(KEY_DATA_INC, aluno.DataInclusao)
            put(KEY_DATA_ULT, LocalDateTime.now().format(formatter).toString())
            put(KEY_ACTIVE, 1)
        }
        db.update(TABLE_ALUNO, values, "$ID_ALUNO = ?", arrayOf(idAluno.toString()))
    }
}