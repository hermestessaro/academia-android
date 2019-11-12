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
import com.example.academia.models.*
import java.lang.Exception
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

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
            "$KEY_DATA_INC date, $KEY_DATA_ULT datetime, $KEY_ACTIVE varchar(4), $KEY_LOGIN not null default 0);"


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

    fun createProfessor(prof: ProfessorModel){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(ID_PROF, prof.IdProfessor)
            put(KEY_NAME, prof.Nome)
            put(KEY_EMAIL, prof.Email)
            put(KEY_SENHA, prof.Senha)
            put(KEY_DATA_INC, prof.DataInclusao)
            put(KEY_DATA_ULT, prof.DataHoraUltimaAtu)
            put(KEY_ACTIVE, prof.IndicadorAtivo)
        }

        try {
            db.insertOrThrow(TABLE_PROF, null, values)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    fun getProfessorByName(name: String) : ProfessorModel {

        val db = this.readableDatabase
        val selectQuery : String = "SELECT * FROM $TABLE_PROF WHERE $KEY_NAME = '$name';"
        val c: Cursor = db.rawQuery(selectQuery, null)
        if(c != null){
            c.moveToFirst()
        }

        val prof = ProfessorModel(c.getInt(c.getColumnIndex(ID_PROF)),
            name,
            c.getString(c.getColumnIndex(KEY_EMAIL)),
            c.getString(c.getColumnIndex(KEY_SENHA)),
            c.getString(c.getColumnIndex(KEY_DATA_INC)),
            c.getString(c.getColumnIndex(KEY_DATA_ULT)),
            c.getString(c.getColumnIndex(KEY_ACTIVE))
        )
        return prof
    }

    fun getProfessorById(Id: Int) : ProfessorModel {
        val db = this.readableDatabase
        val selectQuery : String = "SELECT * FROM $TABLE_PROF WHERE $ID_PROF = '$Id';"
        val c: Cursor = db.rawQuery(selectQuery, null)
        if(c != null){
            c.moveToFirst()
        }

        val prof = ProfessorModel(Id,
            c.getString(c.getColumnIndex(KEY_NAME)),
            c.getString(c.getColumnIndex(KEY_EMAIL)),
            c.getString(c.getColumnIndex(KEY_SENHA)),
            c.getString(c.getColumnIndex(KEY_DATA_INC)),
            c.getString(c.getColumnIndex(KEY_DATA_ULT)),
            c.getString(c.getColumnIndex(KEY_ACTIVE))
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
                    c.getString(c.getColumnIndex(KEY_ACTIVE)))
                professorList.add(prof)
            }while(c.moveToNext())
        }
        return professorList
    }

    fun createAluno(aluno: AlunoModel){
        val db = this.writableDatabase
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
            put(KEY_DATA_ULT, LocalDateTime.now().toString())
            put(KEY_ACTIVE, 1)
        }


        db.insert(TABLE_ALUNO, null, values)

    }

    fun getIdAlunoByName(name: String): Int {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_ALUNO WHERE $KEY_NAME = '$name' AND $KEY_ACTIVE = '1';"
        val c: Cursor = db.rawQuery(selectQuery, null)
        c.moveToLast()
        return c.getInt(c.getColumnIndex(ID_ALUNO))
    }

    fun getAllAlunos() : MutableList<AlunoModel> {
        val db = this.readableDatabase
        Log.d("primeiro", "passou")
        val selectQuery : String = "SELECT * FROM $TABLE_ALUNO WHERE $KEY_ACTIVE = '1';"
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

    fun getAlunoById(idAluno: Int): AlunoModel{
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_ALUNO WHERE $ID_ALUNO = $idAluno AND $KEY_ACTIVE = '1';"
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
        val db = this.readableDatabase
        val alunos: MutableList<AlunoModel> = ArrayList()
        val selectQuery : String = "SELECT * FROM $TABLE_ALUNO WHERE $ID_INCL = $idProf AND $KEY_ACTIVE = '1'"
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
        val db = this.writableDatabase
       /* val whereClause = "$ID_ALUNO=?"
        db.delete(TABLE_ALUNO, whereClause, arrayOf(idAluno.toString()))
        db.delete(TABLE_DISP, whereClause, arrayOf(idAluno.toString()))
        db.delete(TABLE_TREINO, whereClause, arrayOf(idAluno.toString()))
        db.delete(TABLE_EXERCICIO, whereClause, arrayOf(idAluno.toString()))*/
        val updateQuery = "UPDATE $TABLE_ALUNO SET $KEY_ACTIVE = '0' WHERE $ID_ALUNO = $idAluno;"
        db.rawQuery(updateQuery, null)

    }

    fun updateAluno(aluno: AlunoModel, idAluno: Int){
        val db = this.writableDatabase
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
            put(KEY_DATA_ULT, getDateInstance().format(Date()))
            put(KEY_ACTIVE, 1)
        }
        db.update(TABLE_ALUNO, values, "$ID_ALUNO = ?", arrayOf(idAluno.toString()))
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
                val grupo = GrupoModel(c.getInt(c.getColumnIndex(ID_GRUPO)),
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
        val grupo = GrupoModel(c.getInt(c.getColumnIndex(ID_GRUPO)),
                    c.getString(c.getColumnIndex(KEY_NAME)))
        return grupo
    }

    fun createAparelho(aparelho: AparelhoModel): Int{
        if(getAparelhoByName(aparelho) == null){
            return -1
        }
        val db = this.writableDatabase
        val values: ContentValues = ContentValues()
        values.put(ID_GRUPO, aparelho.IdGrupo)
        values.put(KEY_NAME, aparelho.Nome)
        var rowInserted = -1
        try {
            rowInserted = db.insertOrThrow(TABLE_APARELHO, null, values).toInt()
        }catch (e:Exception){
            e.printStackTrace()
        }
        return rowInserted
    }

    fun getAparelhoByName(aparelho: AparelhoModel): AparelhoModel?{
        val db = this.readableDatabase
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
        val db = this.writableDatabase
        val whereClause = "$KEY_NAME=?"
        return db.delete(TABLE_APARELHO, whereClause, arrayOf(nomeAparelho))
    }

    fun getAparelhosByGrupo(nomeGrupo: String): MutableList<AparelhoModel>{
        val grupo = getGrupoByName(nomeGrupo)
        val db = this.readableDatabase
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

    fun getTreinosByIdAluno(idAluno: Int) : MutableList<TreinoModel>{
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_TREINO WHERE $ID_ALUNO = $idAluno"
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

    fun getTreinoById(idTreino: Int, idAluno: Int) : TreinoModel{
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_TREINO WHERE $ID_TREINO = $idTreino AND $ID_ALUNO = $idAluno"
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

    fun getExerciciosByIdTreino(id: Int, idAluno: Int): MutableList<ExercicioModel>{
        val db = this.readableDatabase
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

        val db = this.writableDatabase
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


    fun saveTreino(treino: TreinoModel){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_TREINO, treino.idTreino)
        values.put(ID_PROF, treino.idProf)
        values.put(ID_ALUNO, treino.idAluno)
        values.put(KEY_TIPO, treino.tipo)
        values.put(KEY_NAME, treino.nome)
        val row = db.insert(TABLE_TREINO, null, values)
        Log.d("rowTreino", row.toString())
    }

    fun deleteTreino(idAluno: Int, idTreino: Int) {
        val db = this.writableDatabase
        /*val whereClause = "$ID_ALUNO=? AND $ID_TREINO=?"
        db.delete(TABLE_TREINO, whereClause, arrayOf(idAluno.toString(), idTreino.toString()))
        db.delete(TABLE_EXERCICIO, whereClause, arrayOf(idAluno.toString(), idTreino.toString()))*/
        val updateQuery = "UPDATE $TABLE_TREINO SET $KEY_ACTIVE = '0' WHERE $ID_ALUNO = $idAluno;"
        db.rawQuery(updateQuery, null)
    }

    fun updateTreino(treino: TreinoModel){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_TREINO, treino.idTreino)
        values.put(ID_PROF, treino.idProf)
        values.put(ID_ALUNO, treino.idAluno)
        values.put(KEY_TIPO, treino.tipo)
        values.put(KEY_NAME, treino.nome)
        val whereClause = "$ID_ALUNO=? AND $ID_TREINO=?"
        db.update(TABLE_TREINO, values, whereClause, arrayOf(treino.idAluno.toString(), treino.idTreino.toString()))

    }


    companion object {
        private val DB_NAME = "database.db"
        private val DB_VERSION = 15

    }
}
