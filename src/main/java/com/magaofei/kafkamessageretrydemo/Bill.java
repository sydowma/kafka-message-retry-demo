package com.magaofei.kafkamessageretrydemo;

import java.math.BigDecimal;
import java.util.Objects;

public final class Bill {
    private final String id;
    private final String botId;
    private final String userId;
    private final BigDecimal amount;
    private final int retryTimes;

    public Bill(String id, String botId, String userId, BigDecimal amount, int retryTimes) {
        this.id = id;
        this.botId = botId;
        this.userId = userId;
        this.amount = amount;
        this.retryTimes = retryTimes;
    }

    public String id() {
        return id;
    }

    public String botId() {
        return botId;
    }

    public String userId() {
        return userId;
    }

    public BigDecimal amount() {
        return amount;
    }

    public int retryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        retryTimes = retryTimes;
    }

    // exclude retryTimes
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Bill) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.botId, that.botId) &&
                Objects.equals(this.userId, that.userId) &&
                Objects.equals(this.amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, botId, userId, amount);
    }

    @Override
    public String toString() {
        return "Bill[" +
                "id=" + id + ", " +
                "botId=" + botId + ", " +
                "userId=" + userId + ", " +
                "amount=" + amount + ", " +
                "retryTimes=" + retryTimes + ']';
    }

}
