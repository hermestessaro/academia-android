package com.example.academia

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.academia.models.ProfessorModel

class DatabaseHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {

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
    private val KEY_NAME = "Nome"
    private val KEY_ACTIVE = "IndicadorAtivo"
    private val KEY_DATA_INC = "DataInclusao"
    private val KEY_DATA_ULT = "DataHoraUltimaAtu"

    //Aluno column names
    private val ID_ALUNO = "IdAluno"
    private val ID_INCL = "IdUsuarioInclusao"
    private val KEY_NASCIMENTO = "DataNascimento"
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
    private val CREATE_TABLE_ALUNO = "CREATE TABLE $TABLE_ALUNO ($ID_ALUNO int NOT NULL, " +
            "$KEY_NAME varchar(100), $KEY_NASCIMENTO date, $KEY_IND_1 varchar(1), " +
            "$KEY_IND_2 varchar(1), $KEY_IND_3 varchar(1), $KEY_IND_4 varchar(1), " +
            "$KEY_IND_5 varchar(1), $KEY_IND_6 varchar(1), $KEY_IND_7 varchar(1), " +
            "$KEY_LESOES varchar(200) NULL, $KEY_OBS varchar(200) NULL, $KEY_TREINO_ESP varchar(200) NULL, " +
            "$KEY_DATA_INC date, $KEY_DATA_ULT datetime, $KEY_ACTIVE varchar(1), PRIMARY KEY ($ID_ALUNO));"

    private val CREATE_TABLE_ALUNO_DISP = "CREATE TABLE $TABLE_ALUNO_DISP ($ID_ALUNO_DISP int NOT NULL, " +
            "$ID_ALUNO int, $ID_DISP int, PRIMARY KEY($ID_ALUNO_DISP), " +
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

    private val CREATE_TABLE_APARELHO = "CREATE TABLE $TABLE_APARELHO ($ID_APARELHO int NOT NULL, " +
            "$ID_GRUPO int, $KEY_NAME varchar(45), " +
            "FOREIGN KEY($ID_GRUPO) REFERENCES $TABLE_GRUPO($ID_GRUPO));"

    private val CREATE_TABLE_GRUPO = "CREATE TABLE $TABLE_GRUPO ($ID_GRUPO int NOT NULL, " +
            "$KEY_NAME varchar(20), PRIMARY KEY($ID_GRUPO));"


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_ALUNO)
        db.execSQL(CREATE_TABLE_ALUNO_DISP)
        db.execSQL(CREATE_TABLE_DISP)
        db.execSQL(CREATE_TABLE_PROFESSOR)
        db.execSQL(CREATE_TABLE_TREINO)
        db.execSQL(CREATE_TABLE_FREQ)
        db.execSQL(CREATE_TABLE_EXERCICIO)
        db.execSQL(CREATE_TABLE_APARELHO)
        db.execSQL(CREATE_TABLE_GRUPO)

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

    fun closeDB() {
        val db = this.readableDatabase
        if (db != null && db.isOpen) {
            db.close()
        }
    }


    fun getProfessor(idProf: Int) : ProfessorModel {
        val db : SQLiteDatabase = this.readableDatabase
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
        closeDB()
        return prof

    }

    fun getAllProfessors() : MutableList<ProfessorModel> {
        val db : SQLiteDatabase = this.readableDatabase
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
        closeDB()
        return professorList
    }
}