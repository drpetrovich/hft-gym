package org.tfr.train.utils;

public class AvgProcessor {
    private double avg;
    private double sigma;
    private int n = 0;

    public double getAvg() {
        return avg;
    }

    public double getSigma() {
        return sigma;
    }

    public int getN() {
        return n;
    }

    public void add(double value) {
        n++;
        double prevAvgPrice = avg;
        avg = avg * (n - 1) / n + value / n;
        double d = Math.pow(sigma, 2) + Math.pow(prevAvgPrice, 2) - Math.pow(avg, 2) + (Math.pow(value, 2) - Math.pow(sigma, 2) - Math.pow(prevAvgPrice, 2)) / n;
        if (d < 0) {
            d = -d;
        }
        sigma = Math.sqrt(d);
    }

    public void refresh() {
        avg = 0;
        sigma = 0;
        n = 0;
    }
}
