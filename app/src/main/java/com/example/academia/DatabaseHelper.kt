package com.example.academia

import android.app.PendingIntent.getActivity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.example.academia.models.AlunoModel
import com.example.academia.models.AparelhoModel
import com.example.academia.models.GrupoModel
import com.example.academia.models.ProfessorModel
import java.lang.Exception

class DatabaseHelper(context:Context?): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    //Table names
    private val TABLE_ALUNO = "Aluno"
    private val TABLE_ALUNO_DISP = "AlunoDisponibilidade"
    private val TABLE_DISP = "Disponibilidade"
    private val TABLE_PROF = "Professor"
    private val TABLE_TREINO = "Treino"
    private val TABLE_FREQ = "Frequencia"
    private val TABLE_EXERCICIO = "Exercicio"
    private val TABLE_APARELHO = "Aparelho"
    private val TABLE_GRUPO = "Grupo"

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

    //AlunoDisponibilidade column names
    private val ID_ALUNO_DISP = "IdAlunoDisponibilidade"

    //Disponibilidade column names
    private val ID_DISP = "IdDisponibilidade"

    //Professor column names
    private val ID_PROF = "IdProfessor"
    private val KEY_EMAIL = "Email"
    private val KEY_SENHA = "Senha"

    //Treino column names
    private val ID_TREINO = "IdTreino"
    private val KEY_OBJETIVOS = "Objetivos"

    //Frequencia column names
    private val ID_FREQ = "IdFrequencia"
    private val DATA_FREQ = "DataFrequencia"

    //Exercicio column names
    private val ID_EXERCICIO = "IdExercicio"
    private val KEY_SERIES = "Series"
    private val KEY_REPS = "Repeticoes"
    private val KEY_PESO = "Peso"

    //Aparelho column names
    private val ID_APARELHO = "IdAparelho"

    //Grupo column names
    private val ID_GRUPO = "IdGrupo"

    //Table create statements
    private val CREATE_TABLE_ALUNO = "CREATE TABLE $TABLE_ALUNO ($ID_ALUNO INTEGER PRIMARY KEY, " +
            "$KEY_NAME varchar(100), $KEY_NASCIMENTO date, $KEY_PROF varchar(100), $KEY_IND_1 varchar(1), " +
            "$KEY_IND_2 varchar(1), $KEY_IND_3 varchar(1), $KEY_IND_4 varchar(1), " +
            "$KEY_IND_5 varchar(1), $KEY_IND_6 varchar(1), $KEY_IND_7 varchar(1), " +
            "$KEY_LESOES varchar(200) NULL, $KEY_OBS varchar(200) NULL, $KEY_TREINO_ESP varchar(200) NULL);"
            //"$KEY_DATA_INC date, $KEY_DATA_ULT datetime, $KEY_ACTIVE varchar(1));"

    private val CREATE_TABLE_ALUNO_DISP = "CREATE TABLE $TABLE_ALUNO_DISP ($ID_ALUNO_DISP INTEGER PRIMARY KEY, " +
            "$ID_ALUNO int, $ID_DISP int, " +
            "FOREIGN KEY($ID_ALUNO) REFERENCES $TABLE_ALUNO($ID_ALUNO), " +
            "FOREIGN KEY($ID_DISP) REFERENCES $TABLE_DISP($ID_DISP));"

    private val CREATE_TABLE_DISP = "CREATE TABLE $TABLE_DISP ($ID_DISP int NOT NULL, " +
            "$KEY_NAME varchar(7), PRIMARY KEY($ID_DISP));"

    private val CREATE_TABLE_PROFESSOR = "CREATE TABLE $TABLE_PROF ($ID_PROF int NOT NULL, " +
            "$KEY_NAME varchar(100), $KEY_EMAIL varchar(45), $KEY_SENHA varchar(255), " +
            "$KEY_DATA_INC date, $KEY_DATA_ULT datetime, $KEY_ACTIVE varchar(1), PRIMARY KEY ($ID_PROF));"

    private val CREATE_TABLE_TREINO = "CREATE TABLE $TABLE_TREINO ($ID_TREINO int NOT NULL, " +
            "$ID_PROF int, $ID_ALUNO int, $KEY_NAME varchar(45) NULL, $KEY_OBJETIVOS varchar(200) NULL, " +
            "$KEY_DATA_INC date, $KEY_DATA_ULT datetime, $KEY_ACTIVE varchar(1), PRIMARY KEY ($ID_TREINO)" +
            "FOREIGN KEY($ID_PROF) REFERENCES $TABLE_PROF($ID_PROF), " +
            "FOREIGN KEY($ID_ALUNO) REFERENCES $TABLE_ALUNO($ID_ALUNO));"

    private val CREATE_TABLE_FREQ = "CREATE TABLE $TABLE_FREQ ($ID_FREQ int NOT NULL, " +
            "$ID_TREINO int, $DATA_FREQ datetime," +
            "FOREIGN KEY($ID_TREINO) REFERENCES $TABLE_TREINO($ID_TREINO));"

    private val CREATE_TABLE_EXERCICIO = "CREATE TABLE $TABLE_EXERCICIO ($ID_EXERCICIO int NOT NULL, " +
            "$ID_TREINO int, $ID_APARELHO int, $KEY_SERIES int, $KEY_REPS int, $KEY_PESO varchar(3), " +
            "$KEY_DATA_ULT datetime, $KEY_ACTIVE varchar(1), PRIMARY KEY($ID_EXERCICIO), " +
            "FOREIGN KEY($ID_TREINO) REFERENCES $TABLE_TREINO($ID_TREINO)," +
            "FOREIGN KEY($ID_APARELHO) REFERENCES $TABLE_APARELHO($ID_APARELHO));"

    private val CREATE_TABLE_APARELHO = "CREATE TABLE $TABLE_APARELHO ($ID_APARELHO INTEGER PRIMARY KEY, " +
            "$ID_GRUPO int, $KEY_NAME varchar(45) UNIQUE, " +
            "FOREIGN KEY($ID_GRUPO) REFERENCES $TABLE_GRUPO($ID_GRUPO));"

    private val CREATE_TABLE_GRUPO = "CREATE TABLE $TABLE_GRUPO ($ID INTEGER PRIMARY KEY, " +
            "$KEY_NAME varchar(20) UNIQUE);"


    override fun onCreate(db: SQLiteDatabase) {
        db.run {
            execSQL("DROP TABLE IF EXISTS $TABLE_ALUNO")
            execSQL("DROP TABLE IF EXISTS $TABLE_ALUNO_DISP")
            execSQL("DROP TABLE IF EXISTS $TABLE_DISP")
            execSQL("DROP TABLE IF EXISTS $TABLE_PROF")
            execSQL("DROP TABLE IF EXISTS $TABLE_TREINO")
            execSQL("DROP TABLE IF EXISTS $TABLE_FREQ")
            execSQL("DROP TABLE IF EXISTS $TABLE_EXERCICIO")
            execSQL("DROP TABLE IF EXISTS $TABLE_APARELHO")
            execSQL("DROP TABLE IF EXISTS $TABLE_GRUPO")
            execSQL(CREATE_TABLE_ALUNO)
            execSQL(CREATE_TABLE_ALUNO_DISP)
            execSQL(CREATE_TABLE_DISP)
            execSQL(CREATE_TABLE_PROFESSOR)
            execSQL(CREATE_TABLE_TREINO)
            execSQL(CREATE_TABLE_FREQ)
            execSQL(CREATE_TABLE_EXERCICIO)
            execSQL(CREATE_TABLE_APARELHO)
            execSQL(CREATE_TABLE_GRUPO)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.run {
            execSQL("DROP TABLE IF EXISTS $TABLE_ALUNO")
            execSQL("DROP TABLE IF EXISTS $TABLE_ALUNO_DISP")
            execSQL("DROP TABLE IF EXISTS $TABLE_DISP")
            execSQL("DROP TABLE IF EXISTS $TABLE_PROF")
            execSQL("DROP TABLE IF EXISTS $TABLE_TREINO")
            execSQL("DROP TABLE IF EXISTS $TABLE_FREQ")
            execSQL("DROP TABLE IF EXISTS $TABLE_EXERCICIO")
            execSQL("DROP TABLE IF EXISTS $TABLE_APARELHO")
            execSQL("DROP TABLE IF EXISTS $TABLE_GRUPO")
        }

        onCreate(db)
    }



    fun getProfessor(idProf: Int) : ProfessorModel {
        val db = this.readableDatabase
        val selectQuery : String = "SELECT * FROM $TABLE_PROF WHERE $ID_PROF = $idProf;"
        val c: Cursor = db.rawQuery(selectQuery, null)
        if(c != null){
            c.moveToFirst()
        }

        val prof = ProfessorModel(idProf,
            c.getString(c.getColumnIndex(KEY_NAME)),
            c.getString(c.getColumnIndex(KEY_EMAIL)),
            c.getString(c.getColumnIndex(KEY_SENHA)),
            c.getString(c.getColumnIndex(KEY_DATA_INC)),
            c.getString(c.getColumnIndex(KEY_DATA_ULT)),
            c.getString(c.getColumnIndex(KEY_ACTIVE))!!.toBoolean()
        )
        return prof

    }

    fun getAllProfessors() : MutableList<ProfessorModel> {
        val db = this.readableDatabase
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
                    c.getString(c.getColumnIndex(KEY_ACTIVE))!!.toBoolean())
                professorList.add(prof)
            }while(c.moveToNext())
        }
        return professorList
    }

    fun createAluno(aluno: AlunoModel){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, aluno.nome)
            put(KEY_NASCIMENTO, aluno.dataNascimento)
            put(KEY_PROF, "Renato")
            put(KEY_IND_1, aluno.dorPeitoAtividades)
            put(KEY_IND_2, aluno.dorPeitoUltimoMes)
            put(KEY_IND_3, aluno.perdaConsciencia)
            put(KEY_IND_4, aluno.problemaArticular)
            put(KEY_IND_5, aluno.tabagista)
            put(KEY_IND_6, aluno.diabetico)
            put(KEY_IND_7, aluno.familiarCardiaco)
            put(KEY_LESOES, aluno.lesoes)
            put(KEY_OBS, aluno.observacoes)
            put(KEY_TREINO_ESP, aluno.treinoEspecifico)
        }
        //values.put(KEY_DATA_INC, aluno.dataInclusao)
        //values.put(KEY_ACTIVE, 1)

        db.insert(TABLE_ALUNO, null, values)

    }

    fun getAllAlunos() : MutableList<AlunoModel> {
        val db = this.readableDatabase
        Log.d("primeiro", "passou")
        val selectQuery : String = "SELECT * FROM $TABLE_ALUNO;"
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
                    c.getString(c.getColumnIndex(KEY_PROF)),
                    ind1.toBoolean(),
                    ind2.toBoolean(),
                    ind3.toBoolean(),
                    ind4.toBoolean(),
                    ind5.toBoolean(),
                    ind6.toBoolean(),
                    ind7.toBoolean(),
                    c.getString(c.getColumnIndex(KEY_LESOES)),
                    c.getString(c.getColumnIndex(KEY_OBS)),
                    c.getString(c.getColumnIndex(KEY_TREINO_ESP)))
                    //c.getString(c.getColumnIndex(KEY_DATA_INC)),
                    //c.getString(c.getColumnIndex(KEY_ACTIVE))!!.toBoolean())
                alunosList.add(aluno)



            }while(c.moveToNext())
        }

        return alunosList
    }

    fun createGrupo(nomeGrupo: String){
        val db = this.writableDatabase
        val values: ContentValues = ContentValues()
        values.put(KEY_NAME, nomeGrupo)
        try {
            db.insertOrThrow(TABLE_GRUPO, null, values)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    fun getAllGrupos() : MutableList<GrupoModel>{
        val db = this.readableDatabase
        val selectQuery : String = "SELECT * FROM $TABLE_GRUPO;"
        val c: Cursor = db.rawQuery(selectQuery, null)
        val gruposList: MutableList<GrupoModel> = ArrayList()
        if(c.moveToFirst()){
            do {
                val grupo = GrupoModel(c.getInt(c.getColumnIndex(ID)),
                            c.getString(c.getColumnIndex(KEY_NAME)))
                gruposList.add(grupo)
            }while (c.moveToNext())
        }
        return gruposList
    }

    fun getGrupoByName(nomeGrupo: String): GrupoModel{
        val db = this.readableDatabase
        val selectQuery : String = "SELECT * FROM $TABLE_GRUPO WHERE $KEY_NAME = '$nomeGrupo';"
        val c: Cursor = db.rawQuery(selectQuery, null)
        c.moveToFirst()
        val grupo = GrupoModel(c.getInt(c.getColumnIndex(ID)),
                    c.getString(c.getColumnIndex(KEY_NAME)))
        return grupo
    }

    fun createAparelho(nomeAparelho:String, nomeGrupo:String): Int{
        val grupo = getGrupoByName(nomeGrupo)
        Log.d("grupoNome", grupo.nome)
        val db = this.writableDatabase
        val values: ContentValues = ContentValues()
        values.put(ID_GRUPO, grupo.id)
        values.put(KEY_NAME, nomeAparelho)
        val rowInserted = db.insert(TABLE_APARELHO, null, values)
        return rowInserted.toInt()
    }

    fun deleteAparelho(nomeAparelho: String): Int{
        val db = this.writableDatabase
        val whereClause = "$KEY_NAME=?"
        return db.delete(TABLE_APARELHO, whereClause, arrayOf(nomeAparelho))
    }

    fun getAparelhosByGrupo(nomeGrupo: String): MutableList<AparelhoModel>{
        val grupo = getGrupoByName(nomeGrupo)
        val db = this.readableDatabase
        val aparelhos: MutableList<AparelhoModel> = ArrayList()
        val idGrupo = grupo.id
        val selectQuery : String = "SELECT * FROM $TABLE_APARELHO WHERE $ID_GRUPO = '$idGrupo'"
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

    companion object {
        private val DB_NAME = "database.db"
        private val DB_VERSION = 1

    }
}
