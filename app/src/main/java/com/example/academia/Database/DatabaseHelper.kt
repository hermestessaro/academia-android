package com.example.academia.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.academia.models.*
import java.lang.Exception
import java.text.DateFormat.getDateInstance
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class DatabaseHelper(context:Context?): SQLiteOpenHelper(context,
    DB_NAME, null,
    DB_VERSION
) {

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
    private val KEY_SEG = "Segunda"
    private val KEY_TER = "Terca"
    private val KEY_QUA = "Quarta"
    private val KEY_QUI = "Quinta"
    private val KEY_SEX = "Sexta"
    private val KEY_SAB = "Sabado"
    private val KEY_DOM = "Domingo"


    //Professor column names
    private val ID_PROF = "IdProfessor"
    private val KEY_EMAIL = "Email"
    private val KEY_SENHA = "Senha"
    private val KEY_LOGIN = "TentativasLogin"

    //Treino column names
    private val ID_TREINO = "IdTreino"
    private val KEY_OBJETIVOS = "Objetivos"
    private val KEY_TIPO = "Identificador"
    private val KEY_APARELHO_NAME = "NomeAparelho"

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
            "$ID_INCL int, $KEY_NAME varchar(100), $KEY_NASCIMENTO date, " +
            "$KEY_IND_1 varchar(1), $KEY_IND_2 varchar(1), $KEY_IND_3 varchar(1), $KEY_IND_4 varchar(1), " +
            "$KEY_IND_5 varchar(1), $KEY_IND_6 varchar(1), $KEY_IND_7 varchar(1), " +
            "$KEY_LESOES varchar(200) NULL, $KEY_OBS varchar(200) NULL, $KEY_TREINO_ESP varchar(200) NULL, " +
            "$KEY_DATA_INC date, $KEY_DATA_ULT date, $KEY_ACTIVE varchar(1), " +
            "FOREIGN KEY($ID_INCL) REFERENCES $TABLE_PROF($ID_PROF));"

    /*private val CREATE_TABLE_ALUNO_DISP = "CREATE TABLE $TABLE_ALUNO_DISP ($ID_ALUNO_DISP INTEGER PRIMARY KEY, " +
            "$ID_ALUNO int, $ID_DISP int, " +
            "FOREIGN KEY($ID_ALUNO) REFERENCES $TABLE_ALUNO($ID_ALUNO), " +
            "FOREIGN KEY($ID_DISP) REFERENCES $TABLE_DISP($ID_DISP));"*/

    private val CREATE_TABLE_DISP = "CREATE TABLE $TABLE_DISP ($ID_DISP INTEGER PRIMARY KEY, " +
            "$KEY_SEG BOOLEAN, $KEY_TER BOOLEAN, $KEY_QUA BOOLEAN, "+
            "$KEY_QUI BOOLEAN, $KEY_SEX BOOLEAN, $KEY_SAB BOOLEAN, "+
            "$KEY_DOM BOOLEAN, $ID_ALUNO INTEGER UNIQUE, "+
            "FOREIGN KEY($ID_ALUNO) REFERENCES $TABLE_ALUNO($ID_ALUNO));"

    private val CREATE_TABLE_PROFESSOR = "CREATE TABLE $TABLE_PROF ($ID_PROF INTEGER PRIMARY KEY, $KEY_NAME varchar(100)" +
            ",$KEY_EMAIL varchar(45) UNIQUE, $KEY_SENHA varchar(255),"+
            "$KEY_DATA_INC date, $KEY_DATA_ULT datetime, $KEY_ACTIVE varchar(1), $KEY_LOGIN not null default 0);"


    private val CREATE_TABLE_TREINO = "CREATE TABLE $TABLE_TREINO ($ID_TREINO INTEGER, " +
            "$ID_PROF int, $ID_ALUNO int, $KEY_TIPO varchar (2), $KEY_NAME varchar(45) NULL," +
            "$KEY_OBJETIVOS varchar(200) NULL, $KEY_DATA_INC date, $KEY_DATA_ULT datetime, $KEY_ACTIVE varchar(1)," +
            "FOREIGN KEY($ID_PROF) REFERENCES $TABLE_PROF($ID_PROF), " +
            "FOREIGN KEY($ID_ALUNO) REFERENCES $TABLE_ALUNO($ID_ALUNO));"

    private val CREATE_TABLE_FREQ = "CREATE TABLE $TABLE_FREQ ($ID_FREQ int NOT NULL, " +
            "$ID_TREINO int, $DATA_FREQ date," +
            "FOREIGN KEY($ID_TREINO) REFERENCES $TABLE_TREINO($ID_TREINO));"

    private val CREATE_TABLE_EXERCICIO = "CREATE TABLE $TABLE_EXERCICIO ($ID_EXERCICIO INTEGER PRIMARY KEY, " +
            "$ID_TREINO int, $ID_ALUNO int, $ID_APARELHO varchar(45), $KEY_SERIES int, $KEY_REPS int, $KEY_PESO int, " +
            "$KEY_DATA_ULT datetime, $KEY_ACTIVE varchar(1)," +
            "FOREIGN KEY($ID_TREINO) REFERENCES $TABLE_TREINO($ID_TREINO)," +
            "FOREIGN KEY($ID_APARELHO) REFERENCES $TABLE_APARELHO($ID_APARELHO));"

    private val CREATE_TABLE_APARELHO = "CREATE TABLE $TABLE_APARELHO ($ID_APARELHO INTEGER PRIMARY KEY, " +
            "$ID_GRUPO int, $KEY_NAME varchar(45) UNIQUE, " +
            "FOREIGN KEY($ID_GRUPO) REFERENCES $TABLE_GRUPO($ID_GRUPO));"

    private val CREATE_TABLE_GRUPO = "CREATE TABLE $TABLE_GRUPO ($ID_GRUPO INTEGER PRIMARY KEY, " +
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

    //PROFESSOR related functions
    fun createProfessor(prof: ProfessorModel){DHProfessor(this.writableDatabase).createProfessor(prof)}
    fun updateProfessor(prof: ProfessorModel, idProf: Int){DHProfessor(this.writableDatabase).updateProfessor(prof, idProf)}
    fun getProfessorByName(name: String) : ProfessorModel? {return DHProfessor(this.readableDatabase).getProfessorByName(name)}
    fun getProfessorById(Id: Int) : ProfessorModel? {return DHProfessor(this.readableDatabase).getProfessorById(Id)}
    fun getAllProfessors() : MutableList<ProfessorModel> {return DHProfessor(this.readableDatabase).getAllProfessors()}

    //ALUNO related functions
    fun createAluno(aluno: AlunoModel){DHAluno(this.writableDatabase).createAluno(aluno)}
    fun updateAluno(aluno: AlunoModel, idAluno: Int){DHAluno(this.writableDatabase).updateAluno(aluno, idAluno)}
    fun deleteAluno(idAluno: Int) {DHAluno(this.writableDatabase).deleteAluno(idAluno)}
    fun getIdAlunoByName(name: String): Int {return DHAluno(this.readableDatabase).getIdAlunoByName(name)}
    fun getAllAlunos() : MutableList<AlunoModel> {return DHAluno(this.readableDatabase).getAllAlunos()}
    fun getAlunoById(idAluno: Int): AlunoModel? {return DHAluno(this.readableDatabase).getAlunoById(idAluno)}
    fun getAlunosByIdProf(idProf: Int): MutableList<AlunoModel> {return DHAluno(this.readableDatabase).getAlunosByIdProf(idProf)}
    fun getLastIdInsertedAluno(): Int {return DHAluno(this.readableDatabase).getLastIdInserted()}

    //GRUPO related functions
    fun createGrupo(nomeGrupo: String){DHGrupo(this.writableDatabase).createGrupo(nomeGrupo)}
    fun getAllGrupos() : MutableList<GrupoModel>{return DHGrupo(this.readableDatabase).getAllGrupos()}
    fun getGrupoByName(nomeGrupo: String): GrupoModel{return DHGrupo(this.readableDatabase).getGrupoByName(nomeGrupo)}

    //APARELHO related functions
    fun createAparelho(aparelho: AparelhoModel): Int{return DHAparelho(this.writableDatabase).createAparelho(aparelho)}
    fun deleteAparelho(nomeAparelho: String): Int{return DHAparelho(this.writableDatabase).deleteAparelho(nomeAparelho)}
    fun getAparelhoByName(aparelho: AparelhoModel): AparelhoModel?{return DHAparelho(this.readableDatabase).getAparelhoByName(aparelho)}
    fun getAparelhosByGrupo(nomeGrupo: String): MutableList<AparelhoModel>{return DHAparelho(this.readableDatabase).getAparelhosByGrupo(nomeGrupo)}

    //TREINO related functions
    fun getTreinosByIdAluno(idAluno: Int) : MutableList<TreinoModel>{ return DHTreino(this.readableDatabase).getTreinosByIdAluno(idAluno)}
    fun getTreinoById(idTreino: Int, idAluno: Int) : TreinoModel { return DHTreino(this.readableDatabase).getTreinoById(idTreino, idAluno)}
    fun saveTreino(treino: TreinoModel) { DHTreino(this.writableDatabase).saveTreino(treino)}
    fun deleteTreino(idAluno: Int, idTreino: Int){ DHTreino(this.writableDatabase).deleteTreino(idAluno, idTreino)}
    fun updateTreino(treino: TreinoModel){DHTreino(this.writableDatabase).updateTreino(treino)}
    fun changeTreinosAluno(oldIdAluno: Int, newIdAluno: Int){DHTreino(this.writableDatabase).changeTreinosAluno(oldIdAluno, newIdAluno)}

    //EXERCICIO related functions
    fun getExerciciosByIdTreino(id: Int, idAluno: Int): MutableList<ExercicioModel>{return DHExercicio(this.readableDatabase).getExerciciosByIdTreino(id, idAluno)}
    fun saveExercicio(exercicio: ExercicioModel){DHExercicio(this.writableDatabase).saveExercicio(exercicio)}


    fun saveDisp(disp: DispModel): Int{
        val db = this.writableDatabase
        val values: ContentValues = ContentValues()
        values.put(KEY_SEG, disp.seg)
        values.put(KEY_TER, disp.ter)
        values.put(KEY_QUA, disp.qua)
        values.put(KEY_QUI, disp.qui)
        values.put(KEY_SEX, disp.sex)
        values.put(KEY_SAB, disp.sab)
        values.put(KEY_DOM, disp.dom)
        values.put(ID_ALUNO, disp.idAluno)
        val rowInserted = db.insert(TABLE_DISP, null, values)
        return rowInserted.toInt()

    }

    fun  getDisp(idAluno: Int): DispModel{
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_DISP WHERE $ID_ALUNO = $idAluno"
        val c: Cursor = db.rawQuery(selectQuery, null)
        c.moveToFirst()
        val disp = DispModel(
            c.getInt(c.getColumnIndex(KEY_SEG)) > 0,
            c.getInt(c.getColumnIndex(KEY_TER))> 0,c.getInt(c.getColumnIndex(KEY_QUA))> 0,
            c.getInt(c.getColumnIndex(KEY_QUI))> 0,c.getInt(c.getColumnIndex(KEY_SEX))> 0,
            c.getInt(c.getColumnIndex(KEY_SAB))> 0,c.getInt(c.getColumnIndex(KEY_DOM))> 0,idAluno)

        return disp
    }



    companion object {
        private val DB_NAME = "database.db"
        private val DB_VERSION = 1

    }
}
