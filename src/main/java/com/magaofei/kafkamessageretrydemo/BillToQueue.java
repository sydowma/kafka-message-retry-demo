package com.magaofei.kafkamessageretrydemo;

public class BillToQueue {

    private final QueueManager<Bill> queueManager;

    public BillToQueue() {
        this.queueManager = new QueueManager<>();
    }

    public void addToFirst(Bill bill) {
        this.queueManager.add(bill.botId(), bill, Double.parseDouble(bill.id()));
    }

    public Bill getLast() {
        return this.queueManager.poll();
    }
}
