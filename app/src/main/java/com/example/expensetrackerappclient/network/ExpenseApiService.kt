package com.example.expensetrackerappclient.network

import com.example.expensetrackerappclient.model.Expense
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ExpenseApiService {

    @GET("expenses")
    suspend fun getExpenses(): List<Expense>

    @POST("expenses")
    suspend fun addExpense(@Body expense: Expense)

    @DELETE("expenses/{id}")
    suspend fun deleteExpenseById(@Path("id") id: String)

    @PUT("expenses")
    suspend fun updateExpense(@Body expense: Expense)

    @GET("expenses/byDate")
    suspend fun getExpensesByDate(@Query("date") date: String): List<Expense>

    @GET("expenses/byCategory")
    suspend fun getExpensesByCategory(@Query("category") category: String): List<Expense>
}