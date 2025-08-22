package com.htsc.transaction.demo.model.dos;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ author: chaote
 * @ date: 2025/8/21  19:44
 * @ <p>交易DAO层<p>
 */

public class TransactionDO {

    //交易id
    private String trxId;

    //交易金额
    private BigDecimal amount;

    //交易状态
    private String status;

    //交易类型
    private String type;

    //交易用户
    private String userId;

    //交易时间
    private Date trxTime;

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

    public Date getTrxTime() {
        return trxTime;
    }

    public void setTrxTime(Date trxTime) {
        this.trxTime = trxTime;
    }

    @Override
    public String toString() {
        return "TransactionDO{" +
                "trxId='" + trxId + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", userId='" + userId + '\'' +
                ", trxTime=" + trxTime +
                '}';
    }
}
