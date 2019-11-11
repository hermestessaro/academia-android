package com.example.academia.WebService

import com.example.academia.models.*
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AppServices {

    @GET("/academia/web/api/professor")
    fun getProfessors(): Deferred<ApiResponse<ProfessorModel>>

    @GET("/academia/web/api/aluno")
    fun getAlunos(): Response<ApiResponse<AlunoModel>>

    @GET("/academia/web/api/exercicio")
    fun getExercicios(): Response<ApiResponse<ExercicioModel>>

    @GET("/academia/web/api/grupo")
    fun getGrupos(): Deferred<ApiResponse<GrupoModel>>

    @GET("/academia/web/api/aparelho")
    fun getAparelhos(@Query("page") page: Int): Deferred<ApiResponse<AparelhoModel>>
}