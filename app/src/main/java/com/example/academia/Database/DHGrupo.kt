package com.example.academia.Database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.academia.models.GrupoModel
import java.lang.Exception

class DHGrupo(val db: SQLiteDatabase) {


    private val TABLE_GRUPO = "Grupo"
    //Common column names
    private val ID = "id"
    private val KEY_NAME = "Nome"
    private val KEY_ACTIVE = "IndicadorAtivo"
    private val KEY_DATA_INC = "DataInclusao"
    private val KEY_DATA_ULT = "DataHoraUltimaAtu"

    //Grupo column names
    private val ID_GRUPO = "IdGrupo"

    private val ID_ALUNO = "IdAluno"
    private val ID_APARELHO = "IdAparelho"


    fun createGrupo(nomeGrupo: String){
        val values: ContentValues = ContentValues()
        values.put(KEY_NAME, nomeGrupo)
        try {
            db.insertOrThrow(TABLE_GRUPO, null, values)
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    fun getAllGrupos() : MutableList<GrupoModel>{
        val selectQuery : String = "SELECT * FROM $TABLE_GRUPO;"
        val c: Cursor = db.rawQuery(selectQuery, null)
        val gruposList: MutableList<GrupoModel> = ArrayList()
        if(c.moveToFirst()){
            do {
                val grupo = GrupoModel(c.getInt(c.getColumnIndex(ID_GRUPO)),
                    c.getString(c.getColumnIndex(KEY_NAME)))
                gruposList.add(grupo)
            }while (c.moveToNext())
        }
        return gruposList
    }

    fun getGrupoByName(nomeGrupo: String): GrupoModel {
        val selectQuery : String = "SELECT * FROM $TABLE_GRUPO WHERE $KEY_NAME = '$nomeGrupo';"
        val c: Cursor = db.rawQuery(selectQuery, null)
        c.moveToFirst()
        val grupo = GrupoModel(c.getInt(c.getColumnIndex(ID_GRUPO)),
            c.getString(c.getColumnIndex(KEY_NAME)))
        return grupo
    }
}