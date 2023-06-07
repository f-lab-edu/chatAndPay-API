package com.chatandpay.api.common

import com.chatandpay.api.domain.Log
import com.chatandpay.api.service.LogService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Aspect
@Component
class ApiResultLoggingAspect () {

    @Autowired
    private lateinit var logService: LogService

    lateinit var logger: Logger

    @Pointcut("execution(* com.chatandpay.api.service.OpenApiService.*(..))")
    private fun openApiCut() {}

    @Before("openApiCut()")
    fun beforeOpenApiWithdrawLog(joinPoint: JoinPoint) {

        val targetClass = joinPoint.target.javaClass
        logger = LoggerFactory.getLogger(targetClass)

        val methodSignature = joinPoint.signature as MethodSignature
        val methodName = methodSignature.method.name
        val parameterTypes = methodSignature.parameterTypes
        val parameterNames = methodSignature.parameterNames
        val argumentValues = joinPoint.args

        logger.info("Entering method: $methodName")

        if (parameterTypes.isEmpty()) {

            val log = Log(
                className = targetClass.toString(),
                methodName = methodName,
                logType = LogType.API_REQUEST
            )

            logService.saveLog(log)
            return
        }

        for (i in parameterTypes.indices) {
            val parameterType = parameterTypes[i]
            val parameterName = parameterNames[i]
            val argumentValue = argumentValues[i]

            logger.info("Parameter: $parameterType $parameterName = $argumentValue")

            val log = Log(
                className = targetClass.toString(),
                methodName = methodName,
                logType = LogType.API_REQUEST,
                argumentValue = argumentValue.toString()
            )

            logService.saveLog(log)

        }
    }

    @AfterReturning(
        pointcut = "execution(* com.chatandpay.api.service.OpenApiService.*(..))",
        returning = "returnObj"
    )
    fun logChargeOpenApiWallet(joinPoint: JoinPoint, returnObj: Any) {

        val targetClass = joinPoint.target.javaClass
        logger = LoggerFactory.getLogger(targetClass)

        val methodSignature = joinPoint.signature as MethodSignature
        val methodName = methodSignature.method.name

        logger.info("Returning Class / Method: $targetClass $methodName")
        logger.info("Returning Type: ${returnObj.javaClass.simpleName}")
        logger.info("Returning Value: $returnObj")

        val log = Log(
            className = targetClass.toString(),
            methodName = methodName,
            logType = LogType.API_RESULT,
            resultValue = returnObj.toString()
        )

        logService.saveLog(log)

    }

}