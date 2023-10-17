package com.example.plugins

import com.example.db.UserService
import com.example.model.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService by inject<UserService>()
    routing {
        route("/"){
            get {
                val users=userService.getUsers()
                call.respond(HttpStatusCode.OK,users)
            }
            post {
                val user=call.receive<User>()
                val result=userService.addUser(user)
                if (result != null){
                    call.respond(HttpStatusCode.Created,result)
                }else{
                    call.respond(HttpStatusCode.BadRequest,"Failed!!")
                }
            }
            put {
                val user=call.receive<User>()
                val result=userService.updateUser(user)
                if (result){
                    call.respond(HttpStatusCode.OK,"Update successful")
                }else{
                    call.respond(HttpStatusCode.BadRequest,"Failed!!")
                }
            }
            delete {
                val user=call.receive<User>()
                val result=userService.deleteUser(user)
                if (result){
                    call.respond(HttpStatusCode.OK,"Delete successful")
                }else{
                    call.respond(HttpStatusCode.BadRequest,"Failed!!")
                }
            }
            route("search"){
                get {
                    val query=call.request.queryParameters["q"].toString()
                    val result=userService.searchUser(query)
                    if (result.isNotEmpty()){
                        call.respond(HttpStatusCode.OK,result)
                    }else{
                        call.respondText("Not Found!!")
                    }
                }
            }
        }
    }
}
