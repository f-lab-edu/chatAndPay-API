package com.chatandpay.api.code

import lombok.Getter
import lombok.RequiredArgsConstructor

@RequiredArgsConstructor
@Getter
enum class BankCode(private val bankName: String, private val code: String) {

    BK001("한국은행", "001"),
    BK002("산업은행", "002"),
    BK003("기업은행", "003"),
    BK004("국민은행", "004"),
    BK007("수협은행", "007"),
    BK008("수출입은행", "008"),
    BK011("NH농협은행", "011"),
    BK012("농축협", "012"),
    BK020("우리은행", "020"),
    BK023("SC제일은행", "023"),
    BK027("한국씨티은행", "027"),
    BK081("하나은행", "081"),
    BK088("신한은행", "088"),
    BK089("케이뱅크", "089"),
    BK090("카카오뱅크", "090"),
    BK092("토스뱅크", "092"),
    BK097("오픈", "097");

    val bankCode: String = code
}