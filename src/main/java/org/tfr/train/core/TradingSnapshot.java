package org.tfr.train.core;

import org.tfr.train.utils.StdSharpeRatioProcessor;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class TradingSnapshot {
    private double position = 0;
    private double totalAmount = 0;
    private int fillsCount = 0;
    private double pnl = 0;
    private double totalFee = 0;

    private double maxLoss;
    private double maxProfit;
    private double dropdown;

    private final List<Execution> trades = new LinkedList<>();
    private final StdSharpeRatioProcessor sharpeRatioProcessor = new StdSharpeRatioProcessor();

    private final Stack<PositionLayer> positionLayers = new Stack<>();

    public double getMargin() {
        return pnl;
    }

    public double getSharpe() {
        return sharpeRatioProcessor.getRisk();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public int getFillsCount() {
        return fillsCount;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public double getMaxLoss() {
        return maxLoss;
    }

    public double getMaxProfit() {
        return maxProfit;
    }

    public double getDropdown() {
        return dropdown;
    }

    public List<Execution> getTrades() {
        return trades;
    }

    public Stack<PositionLayer> getPositionLayers() {
        return positionLayers;
    }

    public void addTrade(Execution order) {
        trades.add(order);
        addTrade(order.getDirection(), order.getTradeAmount(), order.getDealPrice(), order.getFee());
    }

    public void addTrade(int direction, double tradeAmount, double dealPrice, double fee) {
        if (direction == 1) {
            position += tradeAmount;
        } else {
            position -= tradeAmount;
        }
        totalAmount += tradeAmount;
        fillsCount++;
        totalFee += fee;

        double margin = getMargin();
        if (margin < maxLoss) {
            maxLoss = margin;
        } else if (margin > maxProfit) {
            maxProfit = margin;
        }
        double currentDD = (maxProfit - margin);
        if (currentDD > dropdown) {
            dropdown = currentDD;
        }

        //position layering
        PositionLayer peekLayer = !positionLayers.isEmpty() ? positionLayers.peek() : null;
        if (peekLayer == null || peekLayer.direction == direction) {
            positionLayers.push(new PositionLayer(direction, tradeAmount, dealPrice));
        } else {
            double restTradeAmount = tradeAmount;
            int layersDirection = peekLayer.direction;
            while (restTradeAmount > 0 && peekLayer != null) {
                if (peekLayer.volume <= restTradeAmount) {
                    //layer fully realized
                    if (layersDirection == 1) {
                        pnl += peekLayer.volume * (dealPrice - peekLayer.price);
                    } else {
                        pnl += peekLayer.volume * (peekLayer.price - dealPrice);
                    }
                    restTradeAmount -= peekLayer.volume;
                    positionLayers.pop();
                    peekLayer = !positionLayers.isEmpty() ? positionLayers.peek() : null;
                } else {
                    //layer partially realized
                    if (layersDirection == 1) {
                        pnl += restTradeAmount * (dealPrice - peekLayer.price);
                    } else {
                        pnl += restTradeAmount * (peekLayer.price - dealPrice);
                    }
                    peekLayer.volume -= restTradeAmount;
                    restTradeAmount = 0;
                }
            }
            if (restTradeAmount > 0) {
                positionLayers.push(new PositionLayer(direction, restTradeAmount, dealPrice));
            }
        }

        double marginNet = pnl - totalFee;
        sharpeRatioProcessor.update(marginNet);
    }

    public double getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "TradingSnapshot{" +
                "pnl=" + pnl +
                ", position=" + position +
                ", totalAmount=" + totalAmount +
                ", fillsCount=" + fillsCount +
                ", totalFee=" + totalFee +
                ", maxLoss=" + maxLoss +
                ", maxProfit=" + maxProfit +
                ", dropdown=" + dropdown +
                ", sharpe=" + sharpeRatioProcessor.getRisk() +
                '}';
    }


    public static class PositionLayer {
        private final int direction;
        private final double price;
        private double volume;

        public int getDirection() {
            return direction;
        }

        public double getVolume() {
            return volume;
        }

        public double getPrice() {
            return price;
        }

        public PositionLayer(int direction, double volume, double price) {
            this.direction = direction;
            this.volume = volume;
            this.price = price;
        }
    }
}
