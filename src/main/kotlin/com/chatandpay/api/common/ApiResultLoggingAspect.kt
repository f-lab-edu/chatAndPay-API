package com.chatandpay.api.common

import com.chatandpay.api.code.AccountAttemptCode
import com.chatandpay.api.domain.AccountApiLog
import com.chatandpay.api.dto.OpenApiDepositWalletDTO
import com.chatandpay.api.repository.ApiResultLogRepository
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

@Aspect
@Component
class ApiResultLoggingAspect (
    private val apiResultLogRepository: ApiResultLogRepository,
    private val transactionTemplate: TransactionTemplate
) {

    @Pointcut("execution(* com.chatandpay.api.service.OpenApiService.withdrawMoney(..))")
    private fun openApiCut() {

    }

    @Before("openApiCut()")
    fun beforeOpenApiWithdrawLog(joinPoint: JoinPoint) {
        transactionTemplate.execute {
            val log = joinPointToAccountApiLog(joinPoint, AccountAttemptCode.TRY.value)
            apiResultLogRepository.saveAccountApiLog(log)
        }
    }

    @AfterReturning(
        pointcut = "execution(* com.chatandpay.api.service.OpenApiService.withdrawMoney(..))",
        returning = "result"
    )
    fun logChargeOpenApiWallet(joinPoint: JoinPoint, result: Any) {
        transactionTemplate.execute {
            val log = joinPointToAccountApiLog(joinPoint, AccountAttemptCode.API_SUCCESS.value)
            apiResultLogRepository.saveAccountApiLog(log)
        }
    }

    @AfterReturning(
        pointcut = "execution(* com.chatandpay.api.service.AccountService.chargeWallet(..))",
        returning = "result"
    )
    fun logChargeDBWallet(joinPoint: JoinPoint, result: Any) {
        val log = joinPointToAccountApiLog(joinPoint, AccountAttemptCode.DB_SUCCESS.value)
        apiResultLogRepository.saveAccountApiLog(log)
    }


    fun joinPointToAccountApiLog(joinPoint: JoinPoint, attemptCode: Int): AccountApiLog {
        val args = joinPoint.args
        val dto = args[0] as OpenApiDepositWalletDTO

        val payUser = dto.payUser
        var currentMoney = payUser.wallet?.money ?: 0
        val depositMoney = dto.depositMoney

        currentMoney = if (attemptCode == 2) currentMoney - depositMoney else currentMoney
        
        return AccountApiLog(
            null,
            payUser.id!!,
            dto.accountId,
            currentMoney,
            currentMoney + depositMoney,
            attemptCode
        )
    }




}