package com.chatandpay.api.utils

import kotlin.random.Random

class RandomUtil {
    companion object {
        fun generateRandomSixDigits(): String {
            val randomInt = Random.nextInt(1000000)
            return String.format("%06d", randomInt)
        }

        fun generateRandomNineDigits(): String {
            val randomInt = Random.nextInt(1000000000)
            return String.format("%09d", randomInt)
        }
    }
}
