package org.tfr.train.core;

public class Deal {

    private long moment;

    private double price;

    private double amount;

    public Deal() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getMoment() {
        return moment;
    }

    public void setMoment(long moment) {
        this.moment = moment;
    }

    @Override
    public String toString() {
        return "Deal{" +
                "moment=" + moment +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }

}
