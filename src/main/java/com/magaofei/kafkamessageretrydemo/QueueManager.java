package com.magaofei.kafkamessageretrydemo;

import java.util.*;

public class QueueManager<R> {

    public void removeLastElement(String botId, Bill bill) {

        PriorityQueue<ScoredItem<R>> scoredItems = this.queueEntries.get(botId);
        ScoredItem<R> scoredItem = scoredItems.peek();
        if (scoredItem != null && (scoredItem.item).equals(bill)) {
            scoredItems.poll();
        } else {
            throw new RuntimeException("removeLastElement failed");
        }
    }

    public void replaceLastElement(String botId, String id, R bill) {

        PriorityQueue<ScoredItem<R>> scoredItems = this.queueEntries.get(botId);
        ScoredItem<R> peek = scoredItems.peek();
        if (peek != null && peek.item.equals(bill)) {
            // replace the last element
            // TODO performance
            scoredItems.poll();
            scoredItems.add(new ScoredItem<>(bill, Double.parseDouble(id)));
            peek = scoredItems.peek();
            assert Objects.requireNonNull(peek).item.equals(bill);
        } else {
            throw new RuntimeException("replaceLastElement failed");
        }
    }

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

    public R peek() {
        for (Map.Entry<String, PriorityQueue<ScoredItem<R>>> entry : queueEntries.entrySet()) {
            ScoredItem<R> polledItem = entry.getValue().peek();
            if (polledItem != null) {
                return polledItem.item;
            }
        }
        return null;
    }
}
