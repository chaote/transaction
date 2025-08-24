package com.hsbc.transaction.demo.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * @ author: chaote
 * @ date: 2025/8/20  19:19
 * @ <p>交易创建请求记录类<p>
 */
public record TransactionCreateReq(
        @NotBlank String trxId,
        @NotNull BigDecimal amount,
        @NotNull String status,
        @NotNull String type,
        @NotBlank String userId) {
}
