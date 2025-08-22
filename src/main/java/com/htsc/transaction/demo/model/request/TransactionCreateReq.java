package com.htsc.transaction.demo.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * @ author: chaote
 * @ date: 2025/8/20  19:19
 * @ <p><p>
 */
public class TransactionCreateReq{

    /**
     * 交易id
     */
    @NotBlank
    private String trxId;
    /**
     * 交易金额
     */
    @NotNull
    private BigDecimal amount;
    /**
     * 交易状态
     */
    @NotNull
    private String status;
    /**
     * 交易类型
     */
    @NotNull
    private String type;
    /**
     * 交易用户
     */
    @NotBlank
    private String userId;

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TransactionCreateReq{" +
                "trxId='" + trxId + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
