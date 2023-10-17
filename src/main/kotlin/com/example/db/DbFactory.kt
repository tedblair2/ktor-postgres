package com.example.db

import com.example.model.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DbFactory {
    fun init(config:ApplicationConfig){
        val driverClass=config.property("storage.driverClassName").getString()
        val jdbcUrl=config.property("storage.jdbcURL").getString()
        val db=Database.connect(createHikariDataSource(jdbcUrl,driverClass))
        transaction(db){
            SchemaUtils.create(Users)
        }
    }

    private fun createHikariDataSource(url:String,driver:String):HikariDataSource{
        val hikariConfig=HikariConfig().apply {
            driverClassName=driver
            jdbcUrl=url
            maximumPoolSize=3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(hikariConfig)
    }

    suspend fun <T> dbQuery(block:suspend ()->T): T{
        return newSuspendedTransaction(Dispatchers.IO) { block() }
    }
}