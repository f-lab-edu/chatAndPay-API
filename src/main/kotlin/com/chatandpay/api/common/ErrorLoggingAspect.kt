package com.chatandpay.api.common

import com.chatandpay.api.domain.Log
import com.chatandpay.api.service.LogService
import lombok.extern.slf4j.Slf4j
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Slf4j
@Aspect
@Component
class ErrorLoggingAspect {

    @Autowired
    private lateinit var logService: LogService

    lateinit var logger: Logger

    @Pointcut("execution(* com.chatandpay.api.exception..*.*(..))")
    private fun errorCut() {}

    @AfterReturning(value = "errorCut()", returning = "returnObj")
    fun afterErrorReturnLog(joinPoint: JoinPoint, returnObj: Any) {

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
            logType = LogType.ERROR,
            resultValue = returnObj.toString()
        )

        logService.saveLog(log)

    }




}