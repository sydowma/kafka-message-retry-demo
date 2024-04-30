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
        return this.queueManager.peek();
    }

    public void removeLast(Bill bill) {
        this.queueManager.removeLastElement(bill.botId(), bill);
    }

    public void replace(Bill bill) {
        this.queueManager.replaceLastElement(bill.botId(), bill.id(), bill);
    }
}
