package com.chatandpay.api.code

enum class BankRspCode(
    private val content: String,
    private val code: String,
    private val systemUseYn: Boolean
) {

    RSP000("정상", "000", true),
    RSP111("출금(개설)기관 SYSTEM 장애", "111", false),
    RSP114("출금(개설)기관 서비스 시간 아님", "114", false),
    RSP400("입금 처리 중", "400", false),
    RSP452("비밀번호 입력횟수 초과", "452", false),
    RSP453("예금잔액 부족", "453", false),
    RSP454("출금가능잔액 부족", "454", false),
    RSP818("시뮬레이터 응답전문 존재하지 않음", "818", false),
    RSP999("순이체한도 초과", "999", false);

    val msg: String = content
    val bankRspCode: String = code
}