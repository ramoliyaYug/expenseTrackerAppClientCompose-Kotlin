package com.example.expensetrackerappclient.repository

import com.example.expensetrackerappclient.model.Expense
import com.example.expensetrackerappclient.network.ExpenseApiService
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseApiService: ExpenseApiService
) {
    suspend fun getExpenses() = expenseApiService.getExpenses()

    suspend fun addExpense(expense: Expense) = expenseApiService.addExpense(expense)

    suspend fun deleteExpenseById(id: String) = expenseApiService.deleteExpenseById(id)

    suspend fun updateExpense(expense: Expense) = expenseApiService.updateExpense(expense)

    suspend fun getExpensesByDate(date: String) = expenseApiService.getExpensesByDate(date)

    suspend fun getExpensesByCategory(category: String) = expenseApiService.getExpensesByCategory(category)
}