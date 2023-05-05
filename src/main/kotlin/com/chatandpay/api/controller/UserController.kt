package com.chatandpay.api.controller

import com.chatandpay.api.domain.User
import com.chatandpay.api.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @PostMapping("/login")
    fun loginUser(@RequestBody user: User): ResponseEntity<Any> {

        try {
            userService.login(user)
        } catch(e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity.ok("ok")
    }


    @PostMapping("/signup")
    fun createUser(@RequestBody user: User): ResponseEntity<Any> {

        try {
            userService.register(user)
        } catch(e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity.ok("ok")
    }

    @PatchMapping("/{id}")
    fun updateUser(@PathVariable("id") id: Long, @RequestBody userRequest: User): ResponseEntity<Any> {

        try {
            userService.updateUser(id, userRequest)
        } catch(e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity.ok("ok")
    }


    @DeleteMapping("/{id}")
    fun updateUser(@PathVariable("id") id: Long): ResponseEntity<Any> {

        try {
            userService.deleteUser(id)
        } catch(e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity.ok("ok")
    }

}