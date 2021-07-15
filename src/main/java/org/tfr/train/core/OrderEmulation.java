package org.tfr.train.core;

public class OrderEmulation {

    private long orderId;

    private double price;

    private int direction;

    private double orderAmount;

    private double amountRest;

    private long moment;

    private boolean processed = false;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setAmountRest(double amountRest) {
        this.amountRest = amountRest;
    }

    public void reduceAmountRest(double delta) {
        amountRest -= delta;
    }

    public double getAmountRest() {
        return amountRest;
    }

    public long getMoment() {
        return moment;
    }

    public void setMoment(long moment) {
        this.moment = moment;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void markProcessed() {
        this.processed = true;
    }

    public Execution createExecution() {
        Execution order = new Execution();
        order.setPrice(price);
        order.setOrderAmount(orderAmount);
        order.setAmountRest(amountRest);
        order.setDirection(direction);
        order.setMoment(moment);
        order.setOrderId(orderId);
        return order;
    }

}
