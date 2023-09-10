package com.chatandpay.api.utils.test

import com.github.guepardoapps.kulid.ULID

class Kulid() {

    private var creationTimestamp: Long = System.currentTimeMillis()
    private var ulid: String = ULID().toString()

    fun getCreationTimestamp(): Long {
        return creationTimestamp
    }

}