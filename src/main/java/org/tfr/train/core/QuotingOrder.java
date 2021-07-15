package org.tfr.train.core;

public class QuotingOrder {

    private int name;
    private int direction;

    private long requestId;

    private Double price;
    private Double newPrice;

    //not used, just for tracking
    private double orderAmount;

    private double amountRest;
    private Double newAmount;

    private Long orderId;

    private boolean delivered;
    private boolean removed;
    private long moment;

    public QuotingOrder() {
    }

    @Deprecated
    public QuotingOrder(long requestId, int name,
                        double price, int direction, double orderAmount, long moment) {
        update(price, orderAmount, requestId, moment);
        this.name = name;
        this.direction = direction;
        this.orderAmount = orderAmount;
        this.amountRest = orderAmount;
    }


    public void init(long requestId, int name,
                     double price, int direction, double orderAmount, long moment) {
        update(price, orderAmount, requestId, moment);
        removed = false;
        orderId = null;
        this.price = null;
        this.newAmount = null;
        this.name = name;
        this.direction = direction;
        this.orderAmount = orderAmount;
        this.amountRest = orderAmount;
    }

    public int getName() {
        return name;
    }

    public long getRequestId() {
        return requestId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Double getPrice() {
        return price;
    }

    public Double getNewPrice() {
        return newPrice;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public long getMoment() {
        return moment;
    }

    public double getAmountRest() {
        return amountRest;
    }

    public void setAmountRest(double amountRest) {
        this.amountRest = amountRest;
    }

    public boolean isDelivered() {
        return delivered && !removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    public int getDirection() {
        return direction;
    }

    public void remove(long moment, long requestId) {
        if (removed) {
            return;
        }
        removed = true;
        this.requestId = requestId;
        this.moment = moment;
    }

    public void remain() {
        removed = false;
    }

    public void deliver(Long orderId) {
        if (delivered) {
            return;
        }
        this.orderId = orderId;
        price = newPrice;
        if (newAmount != null) {
            orderAmount = newAmount;
            amountRest = newAmount;
        }
        newPrice = null;
        newAmount = null;
        delivered = true;
    }

    public void revert() {
        if (delivered) {
            return;
        }
        this.newPrice = null;
        this.newAmount = null;
        delivered = true;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("QuotingOrder{");
        sb.append("name='").append(name).append('\'');
        sb.append(", direction=").append(direction);
        sb.append(", requestId=").append(requestId);
        sb.append(", price=").append(price);
        sb.append(", newPrice=").append(newPrice);
        sb.append(", amount=").append(orderAmount);
        sb.append(", amountRest=").append(amountRest);
        sb.append(", newAmount=").append(newAmount);
        sb.append(", orderId=").append(orderId);
        sb.append(", delivered=").append(delivered);
        sb.append(", removed=").append(removed);
        sb.append(", moment=").append(moment);
        sb.append('}');
        return sb.toString();
    }

    private void update(double price, Double amount, long requestId, long moment) {
        this.requestId = requestId;
        newPrice = price;
        if (amount != null) {
            newAmount = amount;
        }
        delivered = false;

        this.moment = moment;
    }

}
