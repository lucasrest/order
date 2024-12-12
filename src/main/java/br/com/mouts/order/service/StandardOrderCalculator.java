package br.com.mouts.order.service;

import br.com.mouts.order.model.Order;
import br.com.mouts.order.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StandardOrderCalculator implements OrderCalculator {

    @Override
    public BigDecimal calculateTotal(Order order) {
        return order.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
