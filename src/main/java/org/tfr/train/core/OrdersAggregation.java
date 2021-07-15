package org.tfr.train.core;

public class OrdersAggregation {

    private double id;

    private double price;

    private double volume;

    private int direction; //1 - buy, 2 - sell

    public OrdersAggregation() {
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OrdersAggregation{");
        sb.append("price=").append(price);
        sb.append(", volume=").append(volume);
        sb.append(", direction=").append(direction);
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }

}
