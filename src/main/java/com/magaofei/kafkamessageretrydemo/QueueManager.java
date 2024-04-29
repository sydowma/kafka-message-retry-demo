package com.magaofei.kafkamessageretrydemo;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueManager<R> {

    /**
     * key: unique id
     * value: zset of java version
     */
    private final Map<String, ArrayBlockingQueue<R>> queueEntries;

    public QueueManager() {
        this.queueEntries = new java.util.HashMap<>(32);
    }

    public void add(String s, R bill) {
        ArrayBlockingQueue<R> queue = queueEntries.get(s);
        if (queue == null) {
            queue = new ArrayBlockingQueue<>(100);
            queueEntries.put(s, queue);
        }
        queue.add(bill);
    }

    public R poll() {
        for (Map.Entry<String, ArrayBlockingQueue<R>> entry : queueEntries.entrySet()) {

            R poll = entry.getValue().poll();
            if (poll != null) {
                return poll;
            }
        }

        return null;
    }
}
