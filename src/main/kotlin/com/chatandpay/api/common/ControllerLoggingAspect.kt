package com.chatandpay.api.common

import lombok.extern.slf4j.Slf4j
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


@Slf4j
@Aspect
@Component
class ControllerLoggingAspect {

    lateinit var logger: Logger

    @Pointcut("execution(* com.chatandpay.api.controller..*.*(..))")
    private fun controllerCut() {
    }

    @Before("controllerCut()")
    fun beforeParameterLog(joinPoint: JoinPoint) {

        val targetClass = joinPoint.target.javaClass
        logger = LoggerFactory.getLogger(targetClass)

        val methodSignature = joinPoint.signature as MethodSignature
        val parameterTypes = methodSignature.parameterTypes
        val parameterNames = methodSignature.parameterNames
        val argumentValues = joinPoint.args

        logger.info("Entering method: ${methodSignature.method.name}")

        for (i in parameterTypes.indices) {
            val parameterType = parameterTypes[i]
            val parameterName = parameterNames[i]
            val argumentValue = argumentValues[i]

            logger.info("Parameter: $parameterType $parameterName = $argumentValue")

        }

    }

    @AfterReturning(value = "controllerCut()", returning = "returnObj")
    fun afterReturnLog(joinPoint: JoinPoint, returnObj: Any) {

        logger.info("Returning Class / Method: ${joinPoint.target.javaClass} ${joinPoint.signature.name}")
        logger.info("Returning Type: ${returnObj.javaClass.simpleName}")
        logger.info("Returning Value: $returnObj")

    }

}