package com.example.academia.Database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.academia.models.ProfessorModel
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DHProfessor(val db: SQLiteDatabase) {

    private val TABLE_PROF = "Professor"

    //Common column names
    private val ID = "id"
    private val KEY_NAME = "Nome"
    private val KEY_ACTIVE = "IndicadorAtivo"
    private val KEY_DATA_INC = "DataInclusao"
    private val KEY_DATA_ULT = "DataHoraUltimaAtu"

    //Professor column names
    private val ID_PROF = "IdProfessor"
    private val KEY_EMAIL = "Email"
    private val KEY_SENHA = "Senha"
    private val KEY_LOGIN = "TentativasLogin"

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


    fun createProfessor(prof: ProfessorModel){
        val values = ContentValues().apply {
            put(ID_PROF, prof.IdProfessor)
            put(KEY_NAME, prof.Nome)
            put(KEY_EMAIL, prof.Email)
            put(KEY_SENHA, prof.Senha)
            put(KEY_DATA_INC, prof.DataInclusao)
            put(KEY_DATA_ULT, prof.DataHoraUltimaAtu)
            put(KEY_ACTIVE, "1")
        }

        try {
            db.insertOrThrow(TABLE_PROF, null, values)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    fun getProfessorByName(name: String) : ProfessorModel? {

        val selectQuery : String = "SELECT * FROM $TABLE_PROF WHERE $KEY_NAME = '$name';"
        val c: Cursor = db.rawQuery(selectQuery, null)
        var prof: ProfessorModel? = null
        if(c.moveToFirst()){
            prof = ProfessorModel(c.getInt(c.getColumnIndex(ID_PROF)),
                name,
                c.getString(c.getColumnIndex(KEY_EMAIL)),
                c.getString(c.getColumnIndex(KEY_SENHA)),
                c.getString(c.getColumnIndex(KEY_DATA_INC)),
                c.getString(c.getColumnIndex(KEY_DATA_ULT)),
                c.getString(c.getColumnIndex(KEY_ACTIVE))
            )
        }
        return prof
    }

    fun getProfessorById(Id: Int) : ProfessorModel? {
        val selectQuery : String = "SELECT * FROM $TABLE_PROF WHERE $ID_PROF = '$Id';"
        val c: Cursor = db.rawQuery(selectQuery, null)
        if(c.moveToFirst()) {
            val prof = ProfessorModel(
                Id,
                c.getString(c.getColumnIndex(KEY_NAME)),
                c.getString(c.getColumnIndex(KEY_EMAIL)),
                c.getString(c.getColumnIndex(KEY_SENHA)),
                c.getString(c.getColumnIndex(KEY_DATA_INC)),
                c.getString(c.getColumnIndex(KEY_DATA_ULT)),
                c.getString(c.getColumnIndex(KEY_ACTIVE))
            )
            return prof
        }
        return null

    }

    fun getAllProfessors() : MutableList<ProfessorModel> {
        val selectQuery : String = "SELECT * FROM $TABLE_PROF;"
        val c: Cursor = db.rawQuery(selectQuery, null)

        val professorList : MutableList<ProfessorModel> = ArrayList()
        if(c.moveToFirst()){
            do {
                val prof = ProfessorModel(c.getInt(c.getColumnIndex(ID_PROF)),
                    c.getString(c.getColumnIndex(KEY_NAME)),
                    c.getString(c.getColumnIndex(KEY_EMAIL)),
                    c.getString(c.getColumnIndex(KEY_SENHA)),
                    c.getString(c.getColumnIndex(KEY_DATA_INC)),
                    c.getString(c.getColumnIndex(KEY_DATA_ULT)),
                    c.getString(c.getColumnIndex(KEY_ACTIVE)))
                professorList.add(prof)
            }while(c.moveToNext())
        }
        return professorList
    }

    fun updateProfessor(prof: ProfessorModel, idProf: Int){
        val values = ContentValues().apply {
            put(ID_PROF, idProf)
            put(KEY_NAME, prof.Nome)
            put(KEY_EMAIL, prof.Email)
            put(KEY_SENHA, prof.Senha)
            put(KEY_DATA_INC, prof.DataInclusao)
            put(KEY_DATA_ULT, LocalDateTime.now().format(formatter).toString())
            put(KEY_ACTIVE, "1")
        }
        db.update(TABLE_PROF, values, "$ID_PROF = ?", arrayOf(idProf.toString()))

    }

}