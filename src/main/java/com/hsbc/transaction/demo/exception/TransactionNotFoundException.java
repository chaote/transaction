package com.hsbc.transaction.demo.exception;

import com.hsbc.transaction.demo.common.ErrorCodeEnums;

/**
 * @ author: chaote
 * @ date: 2025/8/20  19:34
 * @ <p><p>
 */
public class TransactionNotFoundException extends RuntimeException{

    private final String trxId;

    public TransactionNotFoundException(String trxId) {
        super(ErrorCodeEnums.TRANS_NOT_FOUND.getErrorName()+":"+trxId);
        this.trxId = trxId;
    }
}
