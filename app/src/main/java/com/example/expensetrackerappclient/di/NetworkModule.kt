package com.example.expensetrackerappclient.di

import com.example.expensetrackerappclient.network.ExpenseApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val baseUrl = "http://10.0.2.2:8000/"
//    for reel device
//    private val baseUrl = "http://10.73.66.69:8000/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideExpenseApiService(retrofit: Retrofit): ExpenseApiService {
        return retrofit.create(ExpenseApiService::class.java)
    }
}