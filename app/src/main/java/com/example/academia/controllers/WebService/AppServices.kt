package com.example.academia.controllers.WebService

import com.example.academia.models.ProfessorModel
import retrofit2.Call
import retrofit2.http.GET

interface AppServices {

    @GET("profs")
    fun getProfessors(): Call<List<ProfessorModel>>
}