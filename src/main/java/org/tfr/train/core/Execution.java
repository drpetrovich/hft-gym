package org.tfr.train.core;

import java.util.Objects;

public class Execution {

    private long orderId;

    private long moment;

    private int direction; //1 - buy, 2 - sell

    private double price;

    private double orderAmount;

    private double amountRest;

    private double tradeAmount;

    private Double dealPrice;

    private double fee;

    public Execution() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getMoment() {
        return moment;
    }

    public void setMoment(long moment) {
        this.moment = moment;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    //needed for orders refresh only
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    //OrderQty - needed for orders refresh only
    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    //LeavesQty
    public double getAmountRest() {
        return amountRest;
    }

    public void setAmountRest(double amountRest) {
        this.amountRest = amountRest;
    }

    //LastQty
    public double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public Double getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(Double dealPrice) {
        this.dealPrice = dealPrice;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Order{");
        sb.append("orderId=").append(orderId);
        sb.append(", moment=").append(moment);
        sb.append(", direction=").append(direction);
        sb.append(", price=").append(price);
        sb.append(", orderAmount=").append(orderAmount);
        sb.append(", amountRest=").append(amountRest);
        sb.append(", tradeAmount=").append(tradeAmount);
        sb.append(", dealPrice=").append(dealPrice);
        sb.append(", fee=").append(fee);
        sb.append('}');
        return sb.toString();
    }

}
