package com.example.db

import com.example.db.DbFactory.dbQuery
import com.example.model.User
import com.example.model.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserServiceImpl : UserService {

    private fun resultRowToUser(row:ResultRow):User{
        return User(
            name = row[Users.name],
            address = row[Users.address],
            id=row[Users.id],
            age = row[Users.age]
        )
    }
    override suspend fun addUser(user: User): User? = dbQuery{
        val insertStmt=Users.insert {
            it[name]= user.name
            it[address]= user.address
            it[age]=user.age
        }
        insertStmt.resultedValues?.singleOrNull()?.let { resultRowToUser(it) }
    }

    override suspend fun updateUser(user: User): Boolean = dbQuery{
        Users.update({Users.name eq user.name}){
            it[address]=user.address
            it[age]=user.age
        }>0
    }

    override suspend fun deleteUser(user: User): Boolean = dbQuery{
        Users.deleteWhere { name eq user.name }>0
    }

    override suspend fun getUsers(): List<User> =dbQuery {
        Users.selectAll().map { resultRowToUser(it) }
    }

    override suspend fun searchUser(query: String): List<User> = dbQuery{
        Users.select { (Users.name.lowerCase() like "%${query.lowercase()}%") or
                (Users.address.lowerCase() like  "%${query.lowercase()}%") }
            .map { resultRowToUser(it) }
    }
}