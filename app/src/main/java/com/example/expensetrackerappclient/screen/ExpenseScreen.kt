package com.example.expensetrackerappclient.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetrackerappclient.model.Expense
import com.example.expensetrackerappclient.viewmodel.ExpenseViewModel
import java.util.UUID

@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val expenses by viewModel.expenses.collectAsState()
    var selectedDate by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }

    var isEditing by remember { mutableStateOf(false) }
    var editingExpense by remember { mutableStateOf<Expense?>(null) }
    var isAdding by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadExpenses()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isAdding = true
                isEditing = false
                editingExpense = null
                title = ""
                amount = ""
                date = ""
                category = ""
                note = ""
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = {
                        selectedDate = it
                        if (it.isNotBlank()) viewModel.getExpensesByDate(it)
                        else viewModel.loadExpenses()
                    },
                    label = { Text("Date") },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {
                        selectedCategory = it
                        if (it.isNotBlank()) viewModel.getExpensesByCategory(it)
                        else viewModel.loadExpenses()
                    },
                    label = { Text("Category") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(expenses) { expense ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.elevatedCardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = expense.title, style = MaterialTheme.typography.titleLarge)
                            Text(text = "₹${expense.amount}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Date: ${expense.date}")
                            Text(text = "Category: ${expense.category}")
                            if (!expense.note.isNullOrBlank()) {
                                Text("Note: ${expense.note}")
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.deleteExpenseById(expense.id)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                ) {
                                    Text(text = "Delete")
                                }

                                Button(
                                    onClick = {
                                        isEditing = true
                                        isAdding = false
                                        editingExpense = expense
                                        title = expense.title
                                        amount = expense.amount.toString()
                                        date = expense.date
                                        category = expense.category
                                        note = expense.note.orEmpty()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                                ) {
                                    Text(text = "Edit")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isEditing) {
                Text(text = "Edit Expense", style = MaterialTheme.typography.headlineLarge)
                ExpenseForm(
                    title = title,
                    onTitleChange = { title = it },
                    amount = amount,
                    onAmountChange = { amount = it },
                    date = date,
                    onDateChange = { date = it },
                    category = category,
                    onCategoryChange = { category = it },
                    note = note,
                    onNoteChange = { note = it },
                    onSubmit = {
                        editingExpense?.let {
                            viewModel.updateExpense(
                                it.copy(
                                    title = title,
                                    amount = amount.toDoubleOrNull() ?: 0.0,
                                    date = date,
                                    category = category,
                                    note = note
                                )
                            )
                        }
                        isEditing = false
                        editingExpense = null
                    },
                    onCancel = {
                        isEditing = false
                        editingExpense = null
                    }
                )
            }

            if (isAdding) {
                Text(text = "Add New Expense", style = MaterialTheme.typography.headlineLarge)
                ExpenseForm(
                    title = title,
                    onTitleChange = { title = it },
                    amount = amount,
                    onAmountChange = { amount = it },
                    date = date,
                    onDateChange = { date = it },
                    category = category,
                    onCategoryChange = { category = it },
                    note = note,
                    onNoteChange = { note = it },
                    onSubmit = {
                        viewModel.addExpense(
                            Expense(
                                title = title,
                                amount = amount.toDoubleOrNull() ?: 0.0,
                                date = date,
                                category = category,
                                note = note,
                                id = UUID.randomUUID().toString()
                            )
                        )
                        // Clear fields
                        title = ""
                        amount = ""
                        date = ""
                        category = ""
                        note = ""
                        isAdding = false
                    },
                    onCancel = {
                        isAdding = false
                    }
                )
            }
        }
    }
}

@Composable
fun ExpenseForm(
    title: String,
    onTitleChange: (String) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit,
    date: String,
    onDateChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    note: String,
    onNoteChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = title, onValueChange = onTitleChange, label = { Text("Title") })
        OutlinedTextField(value = amount, onValueChange = onAmountChange, label = { Text("Amount") })
        OutlinedTextField(value = date, onValueChange = onDateChange, label = { Text("Date") })
        OutlinedTextField(value = category, onValueChange = onCategoryChange, label = { Text("Category") })
        OutlinedTextField(value = note, onValueChange = onNoteChange, label = { Text("Note") })

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onSubmit) {
                Text("Submit")
            }
            OutlinedButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}
