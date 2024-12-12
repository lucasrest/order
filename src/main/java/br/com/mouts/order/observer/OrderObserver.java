package br.com.mouts.order.observer;

import br.com.mouts.order.model.Order;

public interface OrderObserver {
    void onOrderProcessed(Order order);
}
