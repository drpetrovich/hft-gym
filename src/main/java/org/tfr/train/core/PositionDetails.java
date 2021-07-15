package org.tfr.train.core;

public class PositionDetails {

    private double position;

    public void setPosition(double position) {
        this.position = position;
    }

    public double getPosition() {
        return position;
    }

    public void add(double amount, int direction) {
        if (direction == 1) {
            position += amount;
        } else {
            position -= amount;
        }
    }

    @Override
    public String toString() {
        return "PositionDetails{" +
                "position=" + position +
                '}';
    }
}
