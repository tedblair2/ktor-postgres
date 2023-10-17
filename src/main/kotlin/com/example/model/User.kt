package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(
    val name:String,
    val age:Int,
    val address:String,
    val id:Int=0,
)

object Users:Table(){
    val id=integer("id").autoIncrement()
    val name=varchar("name",200)
    val age=integer("age")
    val address=varchar("address",128)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}
