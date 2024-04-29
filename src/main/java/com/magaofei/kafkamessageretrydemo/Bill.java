package com.magaofei.kafkamessageretrydemo;

import java.math.BigDecimal;

public record Bill(String id, String botId, String userId, BigDecimal amount) {
}
