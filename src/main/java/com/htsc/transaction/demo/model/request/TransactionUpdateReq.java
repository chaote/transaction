package com.htsc.transaction.demo.model.request;

import java.math.BigDecimal;

/**
 * @ author: chaote
 * @ date: 2025/8/21  22:05
 * @ <p>交易更新请求<p>
 */
public class TransactionUpdateReq {

    private String trxId;

    private BigDecimal amount;

    private String status;

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

    @Override
    public String toString() {
        return "TransactionUpdateReq{" +
                "trxId='" + trxId + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
