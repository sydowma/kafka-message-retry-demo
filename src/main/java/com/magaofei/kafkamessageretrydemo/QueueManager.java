package com.magaofei.kafkamessageretrydemo;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueManager<R> {

    /**
     * key: unique id
     * value: zset of java version
     */
    private static class ScoredItem<R> {
        R item;
        double score;

        ScoredItem(R item, double score) {
            this.item = item;
            this.score = score;
        }
    }

    private final Map<String, PriorityQueue<ScoredItem<R>>> queueEntries;

    public QueueManager() {
        this.queueEntries = new HashMap<>();
    }

    public void add(String s, R item, double score) {
        PriorityQueue<ScoredItem<R>> queue = queueEntries.get(s);
        if (queue == null) {
            queue = new PriorityQueue<>(Comparator.comparingDouble(a -> a.score));
            queueEntries.put(s, queue);
        }
        queue.add(new ScoredItem<>(item, score));
    }

    public R poll() {
        for (Map.Entry<String, PriorityQueue<ScoredItem<R>>> entry : queueEntries.entrySet()) {
            ScoredItem<R> polledItem = entry.getValue().poll();
            if (polledItem != null) {
                return polledItem.item;
            }
        }
        return null;
    }
}
