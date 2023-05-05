package com.chatandpay.api.service

import com.chatandpay.api.domain.User
import com.chatandpay.api.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Transactional
    fun register(user : User): User? {

        if (user.name.isEmpty() || user.cellphone.isEmpty()) {
            throw IllegalArgumentException("이름, 휴대전화번호를 모두 입력하세요.")
        }

        if(userRepository.findByCellphone(user.cellphone) != null) {
            throw IllegalArgumentException("이미 존재하는 전화번호입니다.")
        }

        val encodedPassword = passwordEncoder.encode(user.password)
        val regUser = User(name = user.name, password = encodedPassword, userId= "", cellphone = user.cellphone)
        return userRepository.save(regUser)
    }


    fun login(user : User): User? {

        val findUser = user.userId?.let { userRepository.findByUserId(it) }
                        ?: throw EntityNotFoundException("해당 아이디로 가입된 사용자가 없습니다.")

        if(passwordEncoder.matches(user.password, findUser.password)) {
            return findUser
        } else {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

    }

    @Transactional
    fun updateUser(id: Long, userRequest: User) {

        val findUser = userRepository.findById(id) ?: throw EntityNotFoundException("IDX 입력이 잘못되었습니다.")

        if (userRequest.userId?.let { userRepository.existsByUserIdAndIdNot(it, id) } == true){
            throw IllegalArgumentException("이미 존재하는 아이디입니다.")
        }

        if (userRepository.existsByCellphoneAndIdNot(userRequest.cellphone, id)){
            throw IllegalArgumentException("이미 존재하는 전화번호입니다.")
        }

        findUser.userId = userRequest.userId ?: findUser.userId

        if(!userRequest.password.isNullOrEmpty()) {
            val encodedPassword = passwordEncoder.encode(userRequest.password)
            findUser.password = encodedPassword
        } else {
            findUser.password = findUser.password
        }

        findUser.cellphone = userRequest.cellphone

        userRepository.save(findUser)
    }

    @Transactional
    fun deleteUser(id: Long) {

        val findUser = userRepository.findById(id) ?: throw EntityNotFoundException("IDX 입력이 잘못되었습니다.")

        try {
            userRepository.delete(findUser)
        }catch (e : Exception) {
            throw Exception(e.message)
        }


    }


}