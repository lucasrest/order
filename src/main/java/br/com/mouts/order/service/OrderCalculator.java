package br.com.mouts.order.service;

import br.com.mouts.order.model.Order;

import java.math.BigDecimal;

public interface OrderCalculator {
    BigDecimal calculateTotal(Order order);
}
