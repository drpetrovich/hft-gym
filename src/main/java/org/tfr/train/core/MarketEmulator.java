package org.tfr.train.core;

import java.util.*;

public class MarketEmulator implements HistoryConsumer {

    private static final double TAKER_FEE = 0.00025;
    private static final double MAKER_FEE = 0;

    private final MarketListener listener;

    private Long currentTime;

    private long orderIdCounter = 1000;

    private final Map<Long, OrderEmulation> orderEmulations = new HashMap<>();

    private MarketDepth marketDepth = MarketDepth.createMarketDepth();

    public MarketEmulator(MarketListener marketListener) {
        listener = marketListener;
    }

    public long addOrder(int direction, double amount, double price) {
        OrderEmulation orderEmulation = new OrderEmulation();
        orderEmulation.setOrderId(orderIdCounter++);
        orderEmulation.setDirection(direction);
        orderEmulation.setPrice(price);
        orderEmulation.setOrderAmount(amount);
        orderEmulation.setAmountRest(amount);
        orderEmulation.setMoment(currentTime);
        orderEmulations.put(orderEmulation.getOrderId(), orderEmulation);
        return orderEmulation.getOrderId();
    }

    public int delOrder(final long orderId) {
        boolean executed = executeDelOrder(orderId);
        if (!executed) {
            return 14;
        }
        return 0;
    }

    @Override
    public void processDeal(final Deal deal) {
        currentTime = deal.getMoment();
        //orders completion
        Iterator<Map.Entry<Long, OrderEmulation>> iterator = orderEmulations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, OrderEmulation> orderEmulationEntry = iterator.next();
            OrderEmulation orderEmulation = orderEmulationEntry.getValue();
            if (orderEmulation.getDirection() == 1 && deal.getPrice() <= orderEmulation.getPrice() ||
                    orderEmulation.getDirection() == 2 && deal.getPrice() >= orderEmulation.getPrice()) {
                OrderState orderState = completeOrder(orderEmulation, deal);
                if (orderState == OrderState.FILLED) {
                    iterator.remove();
                }
            }
            orderEmulation.markProcessed();
        }
        listener.dealReceived(currentTime, deal);
    }

    @Override
    public void processOrdersAggregation(long moment, List<OrdersAggregation> ordersAggregations, boolean isSnapshot) {
        getMarketDepth(isSnapshot).addLevels(ordersAggregations);
        currentTime = moment;
        fillByDepth();
        listener.orderBookReceived(currentTime, ordersAggregations, isSnapshot);
    }

    public MarketDepth getMarketDepth() {
        return marketDepth;
    }

    private void fillByDepth() {
        Iterator<Map.Entry<Long, OrderEmulation>> iterator = orderEmulations.entrySet().iterator();
        while (iterator.hasNext()) {
            OrderEmulation orderEmulation = iterator.next().getValue();
            if (orderEmulation.isProcessed()) {
                continue;
            }
            if (orderEmulation.getDirection() == 1) {
                Iterator<OrdersAggregation> askLevels = marketDepth.getAskLevels();
                while (askLevels.hasNext()) {
                    OrdersAggregation level = askLevels.next();
                    if (level.getPrice() > orderEmulation.getPrice()) {
                        break;
                    }
                    OrderState orderState = completeOrder(orderEmulation, level);
                    if (orderState == OrderState.FILLED) {
                        iterator.remove();
                        break;
                    }
                }
            } else if (orderEmulation.getDirection() == 2) {
                Iterator<OrdersAggregation> bidLevels = marketDepth.getBidLevels();
                while (bidLevels.hasNext()) {
                    OrdersAggregation level = bidLevels.next();
                    if (level.getPrice() < orderEmulation.getPrice()) {
                        break;
                    }
                    OrderState orderState = completeOrder(orderEmulation, level);
                    if (orderState == OrderState.FILLED) {
                        iterator.remove();
                        break;
                    }
                }
            }
            orderEmulation.markProcessed();
        }
    }

    private boolean executeDelOrder(Long orderId) {
        OrderEmulation orderEmulation = orderEmulations.get(orderId);
        if (orderEmulation == null) {
            return false;
        }
        orderEmulations.remove(orderEmulation.getOrderId());
        return true;
    }

    //complete by order book level
    private OrderState completeOrder(OrderEmulation orderEmulation, OrdersAggregation marketDepthLevel) {
        boolean isTaker = !orderEmulation.isProcessed();
        Execution completedOrder;

        if (isTaker) {
            completedOrder = createFilledOrder(orderEmulation, marketDepthLevel.getPrice(), marketDepthLevel.getVolume(), currentTime);
        } else {
            completedOrder = createFilledOrder(orderEmulation, orderEmulation.getPrice(), orderEmulation.getAmountRest(), currentTime);
        }

        double dealFee = isTaker ? TAKER_FEE : MAKER_FEE;
        completedOrder.setFee(completedOrder.getTradeAmount() * completedOrder.getDealPrice() * dealFee);
        return processCompletedOrder(completedOrder);
    }

    //complete by trade
    private OrderState completeOrder(OrderEmulation orderEmulation, Deal deal) {
        Execution completedOrder;

        if (deal.getAmount() > 0) {
            completedOrder = createFilledOrder(orderEmulation, orderEmulation.getPrice(), deal.getAmount(), currentTime);
        } else {
            return OrderState.NOT_FILLED;
        }

//            logger.debug("Order completed by deal {}", deal);
        completedOrder.setFee(completedOrder.getTradeAmount() * completedOrder.getDealPrice() * MAKER_FEE);
        return processCompletedOrder(completedOrder);
    }

    private OrderState processCompletedOrder(final Execution completedOrder) {
        listener.executionReceived(completedOrder);
        return completedOrder.getAmountRest() == 0 ? OrderState.FILLED : OrderState.PARTIALLY_FILLED;
    }

    private Execution createFilledOrder(OrderEmulation orderEmulation, double tradePrice, double availableAmount, long moment) {
        double completedAmount = (Math.min(availableAmount, orderEmulation.getAmountRest()));
        orderEmulation.reduceAmountRest(completedAmount);
        Execution order = orderEmulation.createExecution();
        order.setDealPrice(tradePrice);
        order.setTradeAmount(completedAmount);
        order.setMoment(moment);
        return order;
    }


    private MarketDepth getMarketDepth(boolean isSnapshot) {
        if (isSnapshot) {
            marketDepth = marketDepth.refresh();
        }
        return marketDepth;
    }

    enum OrderState {
        NOT_FILLED,
        PARTIALLY_FILLED,
        FILLED
    }

}
