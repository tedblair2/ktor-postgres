package com.example.di

import com.example.db.UserService
import com.example.db.UserServiceImpl
import org.koin.dsl.module

val appModule= module {
    single<UserService> {
        UserServiceImpl()
    }
}