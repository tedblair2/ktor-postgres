package com.example

import com.example.db.DbFactory
import com.example.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DbFactory.init(environment.config)
    configureDi()
    configureSerialization()
    configureMonitoring()
    configureRouting()
}
