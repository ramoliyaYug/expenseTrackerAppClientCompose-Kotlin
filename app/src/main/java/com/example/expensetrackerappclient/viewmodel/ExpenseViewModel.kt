package com.example.expensetrackerappclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerappclient.model.Expense
import com.example.expensetrackerappclient.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses : StateFlow<List<Expense>> = _expenses

    fun loadExpenses(){
        viewModelScope.launch {
            _expenses.value = expenseRepository.getExpenses()
        }
    }

    fun addExpense(expense: Expense){
        viewModelScope.launch {
            expenseRepository.addExpense(expense)
            loadExpenses()
        }
    }

    fun deleteExpenseById(id: String){
        viewModelScope.launch {
            expenseRepository.deleteExpenseById(id)
            loadExpenses()
        }
    }

    fun updateExpense(expense: Expense){
        viewModelScope.launch {
            expenseRepository.updateExpense(expense)
            loadExpenses()
        }
    }

    fun getExpensesByDate(date: String){
        viewModelScope.launch {
            _expenses.value = expenseRepository.getExpensesByDate(date)
        }
    }

    fun getExpensesByCategory(category: String){
        viewModelScope.launch {
            _expenses.value = expenseRepository.getExpensesByCategory(category)
        }
    }
}