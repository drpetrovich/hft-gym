## Before you start
1. Install OpenJDK (11 is recommended) and Maven
2. Build project with command *mvn clean install* from project root directory
3. Download history file

## How to use the environment

This is the environment to test your strategy ideas. Before you start,
we assume that you know how the market/limit orders work and understand
the basic logic of the market, how the orders match, what are ask and bid prices,
e. t. c.

## Strategy

First of all, you should implement your trading idea as a strategy. 

To do so, create a strategy class extending *MarketStrategy* in *org.tfr.train.strategies* folder. You can use *DataTestStrategy* as a reference. Strategy class works the following way:

You can override two methods: *orderBookReceived* and *dealReceived*,
which are activated when the strategy receives market data. You can also add 
any additional methods that your strategy needs.

*orderBookReceived* method is activated whenever the strategy receives
orderbook snapshot from the market emulation. It contains *moment*, which is the
timestamp of the update, *aggregations*, which is the list of levels of
orderbook (*OrderAggregation*). Each *OrderAggregation* contains data 
about price and volume of orders on that price and the direction of those 
orders (1 - buy orders, 2 - sell orders). It also contains boolean flag 
*isSnapshot*, which in this case is always true.

You can also get current best ask and best bid prices using market.getMarketDepth().getBestAsk()
and market.getMarketDepth().getBestBid().

*dealReceived* method is activated whenever the strategy receives a deal
from market emulator. It also contains *moment*, which is the timestamp of the deal,
and *deal*, which contains information about the deal: price and amount

Your strategy will generate it's decisions based on this data. To do some trades,
you should send your orders to market emulator using *market.addOrder* method.
It takes 3 arguments: direction (1 - buy, 2 - sell), tradingAmount (how much of asset do
you want to buy or sell) and price (the desired price of your order). It returns the ID 
of your order. You can use this ID to cancel your order later.

If you place a limit order (price is less than best ask for buy orders or greater than best
bid for sell orders), it is send to the market emulator. Until the order is not filled, you
can cancel it using *market.delOrder(ID)*, in this case it will be removed from the emulator.

To control the state of your strategy, you can use the *tradingSnapshot*. It contains
information about your current position, PnL, fee, total number of filled orders and total
traded volume since the beginning of backtest period. PnL doesn't consider your current opened position,
so please check your position!

## Backtest

To see the backtest results of your strategy, you should create the *instance* of strategy in
*BacktestRunner* class. Than run *instance.backtest()* method. It takes one argument, which is path 
to the file with market data. When backtesting is finished, you will see *TradingSnapshot* of your
strategy. You can see PnL, fee, position in the end,total traded amount, number of filled orders,
max loss and max profit, dropdown and sharpe ratio of your strategy. 
