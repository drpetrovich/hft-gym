package org.tfr.train.utils;

public class StdSharpeRatioProcessor {

    private final AvgProcessor avgProcessor;
    private double sharpe;
    private Double lastMargin;

    public StdSharpeRatioProcessor() {
        this.avgProcessor = new AvgProcessor();
    }

    public void update(double margin) {
        if (lastMargin == null) {
            lastMargin = margin;
            return;
        }
        avgProcessor.add(margin - lastMargin);
        this.lastMargin = margin;
        if (avgProcessor.getSigma() != 0) {
            this.sharpe = avgProcessor.getAvg() / avgProcessor.getSigma() * Math.sqrt(avgProcessor.getN());
        }
    }

    public double getRisk() {
        return this.sharpe;
    }

    public double getAvgProfit() {
        return avgProcessor.getAvg();
    }
}
