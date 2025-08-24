package com.hsbc.transaction.demo.exception;

import com.hsbc.transaction.demo.common.ErrorCodeEnums;

/**
 * @ author: chaote
 * @ date: 2025/8/20  19:34
 * @ <p><p>
 */
public class DuplicateTransactionException extends RuntimeException{

    private final String trxId;
    public DuplicateTransactionException(String trxId) {
        super(ErrorCodeEnums.TRANS_DUPLICATE.getErrorName()+":"+trxId);
        this.trxId = trxId;
    }
}
