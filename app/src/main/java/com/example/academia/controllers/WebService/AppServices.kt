package com.example.academia.controllers.WebService

import com.example.academia.models.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET

interface AppServices {

    @GET("/academia/web/api/professor")
    fun getProfessors(): Call<List<ProfessorModel>>

    @GET("/academia/web/api/aluno")
    fun getAlunos(): Call<List<AlunoModel>>

    @GET("/academia/web/api/exercicio")
    fun getExercicios(): Call<List<ExercicioModel>>

    @GET("/academia/web/api/grupo")
    fun getGrupos(): Call<List<GrupoModel>>

    @GET("/academia/web/api/aparelho")
    fun getAparelhos(): Call<List<AparelhoModel>>
}