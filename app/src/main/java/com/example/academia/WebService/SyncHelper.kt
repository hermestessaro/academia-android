package com.example.academia.WebService

import android.util.Log
import com.example.academia.Database.DatabaseHelper
import com.example.academia.models.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SyncHelper(val dbHelper: DatabaseHelper) {

    suspend fun SyncAll(last_sync: LocalDateTime){
        syncProf(last_sync)
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
            Log.d("indativo", it.IndicadorAtivo.toString())
            val aluno = AlunoModel(
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