package com.example.plugins

import com.example.di.appModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureDi(){
    install(Koin){
        modules(appModule)
    }
}