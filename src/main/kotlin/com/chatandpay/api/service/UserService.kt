package com.chatandpay.api.service

import com.chatandpay.api.domain.User
import com.chatandpay.api.domain.sms.SmsAuthentication
import com.chatandpay.api.dto.AuthConfirmRequestDTO
import com.chatandpay.api.repository.AuthRepository
import com.chatandpay.api.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
class UserService(private val userRepository: UserRepository, private val authRepository: AuthRepository, private val payUserService: PayUserService) {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var smsService: SmsService


    @Transactional
    fun register(user : User): User? {

        if (user.name.isEmpty() || user.cellphone.isEmpty()) {
            throw IllegalArgumentException("이름, 휴대전화번호를 모두 입력하세요.")
        }

        if(userRepository.findByCellphone(user.cellphone) != null) {
            throw IllegalArgumentException("이미 존재하는 전화번호입니다.")
        }

        val authData = authRepository.findById(user.verificationId) ?: throw IllegalArgumentException("인증 데이터를 찾을 수 없습니다.")

        if (authData.phoneNumber != user.cellphone) {
            throw IllegalArgumentException("인증을 요청한 휴대폰 번호와 가입 요청 휴대폰 번호가 다릅니다.")
        }

        if (!authData.isVerified) {
            throw IllegalArgumentException("휴대폰 인증이 진행되지 않았습니다.")
        }

        val regUser = User(name = user.name, password = "", userId= "", cellphone = user.cellphone, verificationId = user.verificationId)
        return userRepository.save(regUser)
    }


    fun login(user : User): User? {

        val findUser = user.userId?.let { userRepository.findByUserId(it) }
                        ?: throw EntityNotFoundException("해당 아이디로 가입된 사용자가 없습니다.")

        if(!passwordEncoder.matches(user.password, findUser.password)) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        return findUser

    }

    fun authJoin(user: User): Long? {

        if (userRepository.findByCellphone(user.cellphone) != null)  {
            throw IllegalArgumentException("해당 휴대전화번호로 가입된 사용자가 있습니다.")
        }

        return smsService.authSendSms(user.cellphone)
    }

    fun authLogin(cellphone : String): User? {

        val findUser = userRepository.findByCellphone(cellphone)
            ?: throw EntityNotFoundException("해당 휴대전화번호로 가입된 사용자가 없습니다.")

        smsService.authSendSms(cellphone)

        return findUser
    }

    fun authLoginConfirm(saveObj : AuthConfirmRequestDTO): User? {

        val authConfirm: SmsAuthentication = authConfirm(saveObj)

        return userRepository.findByCellphone(authConfirm.phoneNumber)
            ?: throw EntityNotFoundException("해당 휴대전화번호로 가입된 사용자가 없습니다.")
    }


    fun authConfirm(saveObj : AuthConfirmRequestDTO): SmsAuthentication {

        val findAuth = authRepository.findByCellphone(saveObj.cellphone)
            ?: throw EntityNotFoundException("해당 휴대전화번호로 요청된 인증이 없습니다.")

        if (saveObj.authNumber != findAuth.authNumber) {
            throw IllegalArgumentException("입력한 인증 문자가 일치하지 않습니다.")
        }

        if (findAuth.isVerified) {
            throw IllegalArgumentException("이미 처리된 인증입니다.")
        }

        return smsService.authSendSmsConfirm(findAuth)
            ?: throw RuntimeException("인증 중 오류가 발생하였습니다.")
    }



    @Transactional
    fun updateUser(id: Long, userRequest: User) : User? {

        val findUser = userRepository.findById(id) ?: throw EntityNotFoundException("IDX 입력이 잘못되었습니다.")

        if (!userRequest.userId.isNullOrEmpty()
            && userRequest.userId?.let { userRepository.existsByUserIdAndIdNot(it, id) } == true){
            throw IllegalArgumentException("이미 존재하는 아이디입니다.")
        }

        if (userRepository.existsByCellphoneAndIdNot(userRequest.cellphone, id)){
            throw IllegalArgumentException("이미 존재하는 전화번호입니다.")
        }

        val isIdRegistered = !findUser.userId.isNullOrEmpty() && !findUser.password.isNullOrEmpty()
        val doRegIdAndPw = !userRequest.userId.isNullOrEmpty() || !userRequest.password.isNullOrEmpty()
        val canRegIdAndPw = !userRequest.userId.isNullOrEmpty() && !userRequest.password.isNullOrEmpty()

        val encodedPassword = passwordEncoder.encode(userRequest.password ?: "")

        when {
            !isIdRegistered && doRegIdAndPw -> {
                if (!canRegIdAndPw) {
                    throw IllegalArgumentException("ID / 패스워드는 동시에 등록되어야 합니다.")
                }
                findUser.userId = userRequest.userId
                findUser.password = encodedPassword
            }
            !userRequest.password.isNullOrEmpty() -> {
                findUser.password = encodedPassword
            }
        }

        findUser.cellphone = userRequest.cellphone

        return userRepository.save(findUser)
    }

    @Transactional
    fun deleteUser(id: Long) : Boolean {

        val findUser = userRepository.findById(id) ?: throw EntityNotFoundException("IDX 입력이 잘못되었습니다.")

        try {if(payUserService.isPayUser(id)) {
                payUserService.withdrawPayService(id)
            }
            return userRepository.delete(findUser)
        }catch (e : Exception) {
            throw Exception(e.message)
        }

    }


}