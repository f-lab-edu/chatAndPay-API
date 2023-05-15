package com.chatandpay.api.domain

import lombok.Getter
import lombok.RequiredArgsConstructor

@RequiredArgsConstructor
@Getter
enum class BANK_CODE(private val code: String) {

    한국은행("001"),
    산업은행("002"),
    기업은행("003"),
    국민은행("004"),
    수협은행("007"),
    수출입은행("008"),
    NH농협은행("011"),
    농축협("012"),
    우리은행("020"),
    SC제일은행("023"),
    한국씨티은행("027"),
    하나은행("081"),
    신한은행("088"),
    케이뱅크("089"),
    카카오뱅크("090"),
    토스뱅크("092");

    val bankCode: String = code
}