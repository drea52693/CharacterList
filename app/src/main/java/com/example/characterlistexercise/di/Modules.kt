package com.example.characterlistexercise.di

import com.example.characterlistexercise.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val useCaseModule = module {

}

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}