package br.com.mouts.order.service;

import br.com.mouts.order.model.Order;
import br.com.mouts.order.observer.ExternalBProducer;
import br.com.mouts.order.repository.OrderRepository;
import br.com.mouts.order.utils.OrderStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderCalculator orderCalculator;
    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        Order order = mock(Order.class);
        order.setId(1L);
        when(orderRepository.findByRefId(order.getRefId())).thenReturn(Optional.empty());
        when(orderRepository.save(order)).thenReturn(order);

        Order createdOrder = orderService.createOrder(order);

        verify(orderRepository).save(order);
        assertNotNull(createdOrder);
    }

    @Test
    public void testProcessOrder() {
        Order order = getMock();
        order.setStatus(OrderStatusEnum.PENDENTE);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderCalculator.calculateTotal(order)).thenReturn(BigDecimal.valueOf(100));

        Order processedOrder = orderService.processOrder(1L);

        verify(orderRepository).save(order);
        assertEquals(OrderStatusEnum.PROCESSADO, processedOrder.getStatus());
        assertEquals(BigDecimal.valueOf(100), processedOrder.getTotalAmount());
    }

    private Order getMock() {
        Order mock = new Order();
        mock.setId(1L);
        return mock;
    }
}
