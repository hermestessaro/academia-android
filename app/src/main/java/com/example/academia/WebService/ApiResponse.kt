package com.example.academia.WebService

class ApiResponse<T>(
        val items: List<T>,
        val _meta: Meta
)