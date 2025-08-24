package com.hsbc.transaction.demo.model.request;

import java.math.BigDecimal;

/**
* 
* 
* @ author: chaote
* @ date: 2025/8/24  9:20
* @ <p><p>
*/public record TransactionUpdateReq(
        BigDecimal amount,
        String status
) {
}
