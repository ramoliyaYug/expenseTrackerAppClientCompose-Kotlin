package com.example.expensetrackerappclient.model

data class Expense(
    val id: String,
    val title: String,
    val amount: Double,
    val date: String,
    val category: String,
    val note: String? = null
)
