package br.com.mouts.order.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.List;

import br.com.mouts.order.model.Order;
import br.com.mouts.order.model.Product;
import org.junit.jupiter.api.Test;

public class StandardOrderCalculatorTest {

    private final StandardOrderCalculator calculator = new StandardOrderCalculator();

    @Test
    public void testCalculateTotal() {
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);

        when(product1.getPrice()).thenReturn(BigDecimal.valueOf(10));
        when(product2.getPrice()).thenReturn(BigDecimal.valueOf(20));

        Order order = mock(Order.class);
        when(order.getProducts()).thenReturn(List.of(product1, product2));

        BigDecimal total = calculator.calculateTotal(order);

        assertEquals(BigDecimal.valueOf(30), total);
    }
}
