package org.tfr.train.strategies;

import org.tfr.train.core.Deal;
import org.tfr.train.core.MarketStrategy;
import org.tfr.train.core.OrdersAggregation;

import java.util.List;

public class DataTestStrategy extends MarketStrategy {

    // This is an example of strategy implementation

    // In order to implement you own strategy, you have to override orderBookReceived and
    // dealReceived classes of base MarketStrategy class, which are called whenever backtest
    // receives new orderbook or deal. You can also add some custom methods here, but they
    // won't be connected to the backtest, only to the inner logic of the strategy

    private static final double TRADING_AMOUNT = 1;

    private Long buyId = null; // id of the last buy order placed
    private Long sellId = null; // id of the last sell order placed

    @Override
    public void orderBookReceived(long moment, List<OrdersAggregation> aggregations, boolean isSnapshot) {
        double bestBid = market.getMarketDepth().getBestBid(); // those methods are used to get the market data (here we use only best ask and best bid, but you can use more)
        double bestAsk = market.getMarketDepth().getBestAsk(); // you can get any desired level fom aggregations list, they are in the sane order as in first part (200 bids + 200 asks)
        double position = tradingSnapshot.getPosition(); // current position of the strategy
        if (buyId != null) {
            // here I want to cancel buy order I have placed last time, but you can cancell old orders any other way
            market.delOrder(buyId); // sending delete order command to the backtest
            buyId = null;
        }
        if (sellId != null) {
            // here I cancel last sell order
            market.delOrder(sellId);
            sellId = null;
        }
        if (position == 0) {
            // I check my current position and change strategy logic according to it
            // Sending add order command to the backtester:
            // direction: 1 - buy, 2 - sell
            // trading amount: size of the order (0.2 = 0.2ETH, for example)
            // price: the desired price where we want to place the order
            buyId = market.addOrder(1, TRADING_AMOUNT, bestBid - 1.5); // sending add order command to the backtester
            sellId = market.addOrder(2, TRADING_AMOUNT, bestAsk + 1.5);
        } else if (position > 0) {
            sellId = market.addOrder(2, position, bestAsk + 0.6); // if position is already positive, strategy doesn't buy
        } else {
            buyId = market.addOrder(1, -position, bestBid - 0.6);
        }
    }

    @Override
    public void dealReceived(long moment, Deal deal) {
        // you can also implement some strategy logic when the deal is received
    }



}
