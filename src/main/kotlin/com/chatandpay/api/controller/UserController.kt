package com.chatandpay.api.controller

import com.chatandpay.api.domain.User
import com.chatandpay.api.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @PostMapping("/login")
    fun loginUser(@RequestBody user: User): ResponseEntity<Any> {

        userService.login(user)

        return ResponseEntity.ok("ok")
    }

    @PostMapping("/auth")
    fun authLoginUser(@RequestBody user: User): ResponseEntity<Any> {

        userService.authLogin(user)

        return ResponseEntity.ok("ok")
    }

    @PostMapping("/authJoin")
    fun authJoinUser(@RequestBody user: User): ResponseEntity<Any> {

        userService.authJoin(user)

        return ResponseEntity.ok("ok")
    }

    @PostMapping("/authConfirm")
    fun confirmAuthLoginUser(@RequestBody saveObj : ObjectNode): ResponseEntity<Any> {

        val mapper = ObjectMapper()
        val authNumber = mapper.treeToValue(saveObj["authNumber"], String::class.java)
        val user = mapper.treeToValue(saveObj["user"], User::class.java)

        userService.authLoginConfirm(user, authNumber)

        return ResponseEntity.ok("ok")
    }

    @PostMapping("/signup")
    fun createUser(@RequestBody user: User): ResponseEntity<Any> {

        userService.register(user)

        return ResponseEntity.ok("ok")
    }

    @PatchMapping("/{id}")
    fun updateUser(@PathVariable("id") id: Long, @RequestBody userRequest: User): ResponseEntity<Any> {

        userService.updateUser(id, userRequest)

        return ResponseEntity.ok("ok")
    }


    @DeleteMapping("/{id}")
    fun updateUser(@PathVariable("id") id: Long): ResponseEntity<Any> {

        userService.deleteUser(id)

        return ResponseEntity.ok("ok")
    }

}