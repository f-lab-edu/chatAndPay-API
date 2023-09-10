package com.chatandpay.api.utils.test

import de.huxhorn.sulky.ulid.ULID

class Sulky() {

    private var creationTimestamp: Long = System.currentTimeMillis()
    private var ulid: String = ULID().nextULID()

    fun getCreationTimestamp(): Long {
        return creationTimestamp
    }

}
