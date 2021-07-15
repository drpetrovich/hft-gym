## Purpose of solution

This is framework for development and backtesting high frequency trading strategies in Java.


## Strategy

Refer to *DataTestStrategy* when build new strategy. It just buys below and sell above market.

Implement *MarketStrategy.orderBookReceived* to process order book updates.

Implement *MarketStrategy.dealReceived* to process market trades.

## Backtesting

Create instatnce of your strategy in *BacktestRunner*.

All results are stored in *tradingSnapshot* of *MarketStrategy*.

Trades are available by *tradingSnapshot.getTrades()*
