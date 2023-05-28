package com.chatandpay.api.exception

import com.chatandpay.api.dto.OpenApiDepositWalletDTO
import lombok.Getter
import lombok.RequiredArgsConstructor

@Getter
@RequiredArgsConstructor
class WalletChargeAttemptException(
    override val message: String,
    val openApiDto: OpenApiDepositWalletDTO
) : Exception()