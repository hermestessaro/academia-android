package com.example.academia.WebService

import android.content.SharedPreferences
import android.util.Log
import com.example.academia.Database.DatabaseHelper
import com.example.academia.models.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SyncHelper(val dbHelper: DatabaseHelper) {


    suspend fun syncAll(last_sync: LocalDateTime){
        Log.d("SYNC", "gr")
        //syncProf(last_sync)
        syncAlunos(last_sync)
    }

    suspend fun syncProf(last_sync: LocalDateTime){
        val professors_server = retrieveProfessors().await() as List<ProfessorModel>
        val professors_local = dbHelper.getAllProfessors()
        val professors_server_new: MutableList<ProfessorModel> = mutableListOf()
        val professors_local_new: MutableList<ProfessorModel> = mutableListOf()
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        professors_server.forEach {
            if(last_sync.isBefore(LocalDateTime.parse(it.DataHoraUltimaAtu, dateTimeFormatter))){
                professors_server_new.add(it)
            }
        }

        professors_local.forEach {
            if(last_sync.isBefore(LocalDateTime.parse(it.DataHoraUltimaAtu, dateTimeFormatter))) {
                professors_local_new.add(it)
            }
        }

        professors_server_new.forEach {
            val aux = dbHelper.getProfessorById(it.IdProfessor)
            if(aux != null){
                if(it.Nome == aux.Nome){
                    // mesmo registro, apenas alguma alteração
                    dbHelper.updateProfessor(aux, it.IdProfessor)
                }
                else{
                    // é outro registro
                    aux.IdProfessor = professors_local.size + 1
                    dbHelper.createProfessor(aux)
                    dbHelper.updateProfessor(it, it.IdProfessor)
                }
            }
            else{
                // não tem registro com esse id
                dbHelper.createProfessor(it)
            }
        }

        //TODO: atualizar shared preferences com nova data de last sync
        //TODO: atualizar bancos com listas new
    }

    suspend fun syncAlunos(last_sync: LocalDateTime){
        val alunos_server = retrieveAlunos().await() as List<AlunoModel>
        val alunos_local = dbHelper.getAllAlunos()
        val alunos_server_new: MutableList<AlunoModel> = mutableListOf()
        val alunos_server_alt: MutableList<AlunoModel> = mutableListOf()
        val alunos_local_new: MutableList<AlunoModel> = mutableListOf()
        val alunos_local_alt: MutableList<AlunoModel> = mutableListOf()
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        Log.d("SYNC", "retrieved")

        alunos_server.forEach {

            Log.d("SYNCalunosserver", it.DataInclusao)
            if(last_sync.isBefore(LocalDateTime.parse(it.DataInclusao, dateTimeFormatter))){
                alunos_server_new.add(it)
            }
            else{
                if(last_sync.isBefore(LocalDateTime.parse(it.DataHoraUltimaAtu, dateTimeFormatter))){
                    alunos_server_alt.add(it)
                }
            }

        }

        Log.d("SYNC", "new lists")


        alunos_local?.forEach {
            Log.d("SYNCalunoslocal", it.DataInclusao)
            if(last_sync.isBefore(LocalDateTime.parse(it.DataInclusao, dateTimeFormatter))){
                Log.d("SYNC", "entrou if")
                alunos_local_new.add(it)
            }
            else{
                Log.d("SYNC", "if passed")
                if(last_sync.isBefore(LocalDateTime.parse(it.DataHoraUltimaAtu, dateTimeFormatter))){
                    alunos_local_alt.add(it)
                }
            }
        }


        Log.d("SYNC", "alunos server new iniciando")
        //novo aluno no server, verifica se ja tem um aluno com esse id criado localmente, se não só insere normalmente, se sim insere no local e muda o que tava ali pro final da tabela
        alunos_server_new.forEach {
            val alunoServerNew = AlunoModel(it.IdAluno, it.Nome, it.DataNascimento, it.IdProf, it.IndicadorDorPeitoAtividadesFisicas, it.IndicadorDorPeitoUltimoMes,
                it.IndicadorPerdaConscienciaTontura,it.IndicadorProblemaArticular,it.IndicadorTabagista,it.IndicadorDiabetico,it.IndicadorFamiliarAtaqueCardiaco,
                it.Lesoes,it.Observacoes,it.TreinoEspecifico,it.DataInclusao,it.DataHoraUltimaAtu,it.IndicadorAtivo)

            val aux = dbHelper.getAlunoById(it.IdAluno)
            if(aux == null){
                dbHelper.createAluno(alunoServerNew)
            }
            else{
                dbHelper.updateAluno(alunoServerNew, it.IdAluno)
                aux.IdAluno = dbHelper.getLastIdInsertedAluno() + 1
                dbHelper.createAluno(aux)
                dbHelper.changeTreinosAluno(it.IdAluno, aux.IdAluno)
            }

        }

        Log.d("SYNC", "alunos server alt iniciando")
        //alteração no server, altera no banco local
        alunos_server_alt.forEach {
            val alunoServerAlt = AlunoModel(it.IdAluno, it.Nome, it.DataNascimento, it.IdProf, it.IndicadorDorPeitoAtividadesFisicas, it.IndicadorDorPeitoUltimoMes,
                it.IndicadorPerdaConscienciaTontura,it.IndicadorProblemaArticular,it.IndicadorTabagista,it.IndicadorDiabetico,it.IndicadorFamiliarAtaqueCardiaco,
                it.Lesoes,it.Observacoes,it.TreinoEspecifico,it.DataInclusao,it.DataHoraUltimaAtu,it.IndicadorAtivo)

            dbHelper.updateAluno(alunoServerAlt, it.IdAluno)
        }

        Log.d("SYNC", "alunos local new iniciando")
        //alunos novos no local que não estão no servidor
        alunos_local_new.forEach {
            val alunoNewLocal = AlunoModelServer(it.IdAluno, it.Nome, it.DataNascimento, it.IdProf, changeBoolToString(it.IndicadorDorPeitoAtividadesFisicas),
                changeBoolToString(it.IndicadorDorPeitoUltimoMes),changeBoolToString(it.IndicadorPerdaConscienciaTontura),
                changeBoolToString(it.IndicadorProblemaArticular),changeBoolToString(it.IndicadorTabagista),
                changeBoolToString(it.IndicadorDiabetico),changeBoolToString(it.IndicadorFamiliarAtaqueCardiaco),
                it.Lesoes,it.Observacoes,it.TreinoEspecifico,it.DataInclusao,it.DataHoraUltimaAtu,it.IndicadorAtivo)

            val result = createAlunoServer(alunoNewLocal).await() as AlunoModel
            //se aluno foi criado com id diferente no servidor, tem que atualizar no local
            if(result.IdAluno != it.IdAluno){
                dbHelper.updateAluno(result, result.IdAluno)
                dbHelper.changeTreinosAluno(it.IdAluno, result.IdAluno)
            }

        }

        Log.d("SYNC", "alunos local alt iniciando")
        alunos_local_alt.forEach {
            val alunoAltLocal = AlunoModel(it.IdAluno, it.Nome, it.DataNascimento, it.IdProf, it.IndicadorDorPeitoAtividadesFisicas, it.IndicadorDorPeitoUltimoMes,
                it.IndicadorPerdaConscienciaTontura,it.IndicadorProblemaArticular,it.IndicadorTabagista,it.IndicadorDiabetico,it.IndicadorFamiliarAtaqueCardiaco,
                it.Lesoes,it.Observacoes,it.TreinoEspecifico,it.DataInclusao,it.DataHoraUltimaAtu,it.IndicadorAtivo)

            updateAlunoServer(alunoAltLocal)
        }
        Log.d("SYNC", "deu")


    }

    fun changeBoolToString(value: Boolean): String{
        if(value == true){
            return "1"
        }
        else{
            return "0"
        }
    }


    suspend fun getAll() = GlobalScope.async{
        getAparelhos()
        getProf()
        getGrupos()
        getAlunos()
    }

    suspend fun getProf() {
        val professors = retrieveProfessors().await() as List<ProfessorModel>
        professors.forEach {
            val prof = ProfessorModel(
                it.IdProfessor,
                it.Nome,
                it.Email,
                it.Senha,
                it.DataInclusao,
                it.DataHoraUltimaAtu,
                it.IndicadorAtivo
            )
            Log.d("name", prof.Nome)
            dbHelper.createProfessor(prof)

        }
    }

    fun retrieveProfessors(): Deferred<Any> {
        val retrofit = RetrofitInitializer()

        return GlobalScope.async {
            try {
                val primaryResponse = retrofit.appServices().getProfessors().await()
                return@async primaryResponse.items


                /*if(primaryResponse.isSuccessful){
                    professors = primaryResponse.body()!!.items
                    return@async professors
                }
                else{
                    return@async null
                }*/
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun getGrupos() {

        val grupos = retrieveGrupos().await() as List<GrupoModel>

        grupos.forEach {
            val grupo = GrupoModel(
                it.IdGrupo,
                it.Nome
            )
            Log.d("name", grupo.Nome)
            dbHelper.createGrupo(grupo.Nome)
        }

    }

    fun retrieveGrupos(): Deferred<Any> {
        val retrofit = RetrofitInitializer()

        return GlobalScope.async {
            try {
                val primaryResponse = retrofit.appServices().getGrupos().await()
                return@async primaryResponse.items
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun getAparelhos() {
        val aparelhos = retrieveAparelhos().await() as List<AparelhoModel>

        aparelhos.forEach {
            val aparelho = AparelhoModel(
                it.IdGrupo,
                it.Nome
            )
            Log.d("nomeaparelho", aparelho.Nome)
            val result = dbHelper.createAparelho(aparelho)
            Log.d("resultado", result.toString())
        }

    }

    fun retrieveAparelhos(): Deferred<Any> {
        val retrofit = RetrofitInitializer()

        return GlobalScope.async {
            try {
                val primaryResponse = retrofit.appServices().getAparelhos(1).await()
                val items = primaryResponse.items
                var result = items
                if(primaryResponse._meta.pageCount > 1){
                    for(i in 2 until primaryResponse._meta.pageCount+1){
                        result = result + retrofit.appServices().getAparelhos(i).await().items
                    }
                }
                return@async result
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun getAlunos() {
        val alunos = retrieveAlunos().await() as List<AlunoModel>

        alunos.forEach {
            Log.d("dataincsync", it.DataInclusao)
            val aluno = AlunoModel(it.IdAluno,
                it.Nome,
                it.DataNascimento,
                it.IdProf,
                it.IndicadorDorPeitoAtividadesFisicas,
                it.IndicadorDorPeitoUltimoMes,
                it.IndicadorPerdaConscienciaTontura,
                it.IndicadorProblemaArticular,
                it.IndicadorTabagista,
                it.IndicadorDiabetico,
                it.IndicadorFamiliarAtaqueCardiaco,
                it.Lesoes,
                it.Observacoes,
                it.TreinoEspecifico,
                it.DataInclusao,
                it.DataHoraUltimaAtu,
                it.IndicadorAtivo
            )
            Log.d("nomealuno", aluno.Nome)
            dbHelper.createAluno(aluno)
        }

    }

    fun retrieveAlunos(): Deferred<Any> {
        val retrofit = RetrofitInitializer()

        return GlobalScope.async {
            try {
                val primaryResponse = retrofit.appServices().getAlunos(1).await()
                val items = primaryResponse.items
                var result = items
                if(primaryResponse._meta.pageCount > 1){
                    for(i in 2 until primaryResponse._meta.pageCount+1){
                        result = result + retrofit.appServices().getAlunos(i).await().items
                    }
                }
                return@async result
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun retrieveSingleAluno(idAluno: Int) : Deferred<Any>{
        val retrofit = RetrofitInitializer()

        return GlobalScope.async {
            try {
                val primaryResponse = retrofit.appServices().viewAluno(idAluno).await()
                val items = primaryResponse.items
                val result = items[0]
                return@async result
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun createAlunoServer(aluno: AlunoModelServer) : Deferred<Any>{
        val retrofit = RetrofitInitializer()

        return GlobalScope.async {
            try {
                val primaryResponse = retrofit.appServices().createAluno(aluno).await()
                val items = primaryResponse.items
                val result = items
                return@async result
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateAlunoServer(aluno: AlunoModel){
        val retrofit = RetrofitInitializer()

        GlobalScope.async {
            try{
                val primaryResponse = retrofit.appServices().updateAluno(aluno.IdAluno, aluno).await()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun retrieveDisp(): Deferred<Any> {
        val retrofit = RetrofitInitializer()
        return GlobalScope.async {
            try {
                val primaryResponse = retrofit.appServices().getAlunos(1).await()
                val items = primaryResponse.items
                var result = items
                if(primaryResponse._meta.pageCount > 1){
                    for(i in 2 until primaryResponse._meta.pageCount+1){
                        result = result + retrofit.appServices().getAlunos(i).await().items
                    }
                }
                return@async result
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

    }



    suspend fun getTreinos(){
        val treinos = retrieveTreinos().await() as List<TreinoModel>

        treinos.forEach {
            val treino = TreinoModel(
                it.idTreino,
                it.idProf,
                it.idAluno,
                it.nome,
                it.tipo
            )
            dbHelper.saveTreino(treino)
        }
    }

    fun retrieveTreinos(): Deferred<Any>{
        val retrofit = RetrofitInitializer()

        return GlobalScope.async {
            try {
                val primaryResponse = retrofit.appServices().getTreinos(1).await()
                val items = primaryResponse.items
                var result = items
                if(primaryResponse._meta.pageCount > 1){
                    for(i in 2 until primaryResponse._meta.pageCount+1){
                        result = result + retrofit.appServices().getTreinos(i).await().items
                    }
                }
                return@async result
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}