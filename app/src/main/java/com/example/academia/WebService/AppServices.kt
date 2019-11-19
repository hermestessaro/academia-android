package com.example.academia.WebService

import com.example.academia.models.*
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AppServices {

    @GET("/academia/web/api/professor")
    fun getProfessors(): Deferred<ApiResponse<ProfessorModel>>

    @GET("/academia/web/api/aluno")
    fun getAlunos(@Query("page") page: Int): Deferred<ApiResponse<AlunoModel>>

    @GET("/academia/web/api/aluno/view?id={id}")
    fun viewAluno(@Path("id") id: Int) : Deferred<ApiResponse<AlunoModel>>

    @POST("/academia/web/api/aluno/create")
    fun createAluno(@Body body: AlunoModel): Deferred<ApiResponse<AlunoModel>>

    @PUT("/academia/web/api/aluno/update?id={id}")
    fun updateAluno(@Path("id") id: Int, @Body body: AlunoModel) : Deferred<ApiResponse<AlunoModel>>

    @GET("/academia/web/api/exercicio")
    fun getExercicios(): Response<ApiResponse<ExercicioModel>>

    @GET("/academia/web/api/grupo")
    fun getGrupos(): Deferred<ApiResponse<GrupoModel>>

    @GET("/academia/web/api/aparelho")
    fun getAparelhos(@Query("page") page: Int): Deferred<ApiResponse<AparelhoModel>>

    @GET("/academia/web/api/treino")
    fun getTreinos(@Query("page") page: Int): Deferred<ApiResponse<TreinoModel>>

    @GET("/academia/web/api/disponibilidade")
    fun getDisp(): Deferred<ApiResponse<DispModel>>
}