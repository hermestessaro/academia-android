package com.example.academia.Database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.academia.models.AparelhoModel
import java.lang.Exception

class DHAparelho(val db: SQLiteDatabase) {

    private val TABLE_APARELHO = "Aparelho"
    private val ID = "id"
    private val KEY_NAME = "Nome"
    private val ID_GRUPO = "IdGrupo"


    fun createAparelho(aparelho: AparelhoModel): Int{
        if(getAparelhoByName(aparelho) != null){
            return -1
        }
        val values: ContentValues = ContentValues()
        values.put(ID_GRUPO, aparelho.IdGrupo)
        values.put(KEY_NAME, aparelho.Nome)
        var rowInserted: Long = 0
        try {
            rowInserted = db.insertOrThrow(TABLE_APARELHO, null, values)

        }catch (e: Exception){
            e.printStackTrace()
        }
        return rowInserted.toInt()
    }

    fun getAparelhoByName(aparelho: AparelhoModel): AparelhoModel?{
        val selectQuery : String = "SELECT * FROM $TABLE_APARELHO WHERE $KEY_NAME = '${aparelho.Nome}';"
        val c: Cursor = db.rawQuery(selectQuery, null)
        if(c.moveToFirst()){
            val new = AparelhoModel(
                c.getInt(c.getColumnIndex(ID_GRUPO)),
                c.getString(c.getColumnIndex(KEY_NAME))
            )
            return new
        }
        else return null
    }

    fun deleteAparelho(nomeAparelho: String): Int{
        val whereClause = "$KEY_NAME=?"
        return db.delete(TABLE_APARELHO, whereClause, arrayOf(nomeAparelho))
    }

    fun getAparelhosByGrupo(nomeGrupo: String): MutableList<AparelhoModel>{
        val grupo = DHGrupo(db).getGrupoByName(nomeGrupo)
        val aparelhos: MutableList<AparelhoModel> = ArrayList()
        val idGrupo = grupo.IdGrupo
        val selectQuery : String = "SELECT * FROM $TABLE_APARELHO WHERE $ID_GRUPO = $idGrupo"
        val c: Cursor = db.rawQuery(selectQuery, null)
        if(c.moveToFirst()) {
            do {
                val aparelho = AparelhoModel(c.getInt(c.getColumnIndex(ID_GRUPO)),
                    c.getString(c.getColumnIndex(KEY_NAME)))
                aparelhos.add(aparelho)

            } while (c.moveToNext())
        }

        return aparelhos
    }
}