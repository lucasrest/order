package br.com.mouts.order.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import br.com.mouts.order.model.Order;
import br.com.mouts.order.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

public class OrderFacadeTest {

    @Mock private OrderService orderService;
    @InjectMocks private OrderFacade orderFacade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAndProcessOrder() {
        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(10));

        Order order = mock(Order.class);
        when(orderService.createOrder(any(Order.class))).thenReturn(order);
        when(orderService.processOrder(anyLong())).thenReturn(order);

        Order result = orderFacade.createAndProcessOrder("refId123", List.of(product));

        verify(orderService).createOrder(any(Order.class));
        verify(orderService).processOrder(anyLong());
        assertNotNull(result);
    }
}
