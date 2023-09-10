package com.chatandpay.api.utils.test

class TestUtil {

    fun sulkyMaker() {
        val objects: MutableList<Sulky> = ArrayList()

        for (i in 0..100000) {
            objects.add(Sulky())
        }

        var minTimeDifference = Long.MAX_VALUE
        var maxTimeDifference = Long.MIN_VALUE
        for (i in 1 until objects.size) {
            val timeDifference: Long = objects[i].getCreationTimestamp() - objects[i-1].getCreationTimestamp()
            if (timeDifference < minTimeDifference) {
                minTimeDifference = timeDifference
            }
            if (timeDifference > maxTimeDifference) {
                maxTimeDifference = timeDifference
            }
        }
        println("Sulky 최소차: $minTimeDifference milliseconds")
        println("Sulky 최대차: $maxTimeDifference milliseconds")
    }

    fun kulidMaker() {
        val objects: MutableList<Kulid> = ArrayList()

        for (i in 0..100000) {
            objects.add(Kulid())
        }

        var minTimeDifference = Long.MAX_VALUE
        var maxTimeDifference = Long.MIN_VALUE
        for (i in 1 until objects.size) {
            val timeDifference: Long = objects[i].getCreationTimestamp() - objects[i-1].getCreationTimestamp()
            if (timeDifference < minTimeDifference) {
                minTimeDifference = timeDifference
            }
            if (timeDifference > maxTimeDifference) {
                maxTimeDifference = timeDifference
            }
        }
        println("Kulid 최소차: $minTimeDifference milliseconds")
        println("Kulid 최대차: $maxTimeDifference milliseconds")
    }

    companion object {
        fun createAndPrintSulkyMaker() {
            TestUtil().sulkyMaker()
        }

        fun createAndPrintKulidMaker() {
            TestUtil().kulidMaker()
        }
    }
}
