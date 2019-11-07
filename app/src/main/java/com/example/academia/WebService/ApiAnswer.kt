package com.example.academia.WebService

data class ApiAnswer<T>(
    val action: String,
    val success: String,
    val message: String,
    val data: T
)