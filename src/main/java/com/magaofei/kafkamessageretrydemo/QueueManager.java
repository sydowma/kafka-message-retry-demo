package com.magaofei.kafkamessageretrydemo;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueManager<R> {

    /**
     * key: unique id
     * value: zset of java version
     */
    private final Map<Long, ArrayBlockingQueue<R>> queueEntries;

    public QueueManager() {
        this.queueEntries = new java.util.HashMap<>(32);
    }
}
