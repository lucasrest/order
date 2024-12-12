package br.com.mouts.order.service;

import br.com.mouts.order.factory.OrderFactory;
import br.com.mouts.order.model.Order;
import br.com.mouts.order.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderFacade {

    @Autowired
    private OrderService orderService;

    public Order createAndProcessOrder(String refId, List<Product> products) {
        Order newOrder = OrderFactory.createOrder(refId, products);
        Order order = orderService.createOrder(newOrder);
        return orderService.processOrder(order.getId());
    }

    public Order getOrder(Long orderId) {
        return orderService.getOrder(orderId);
    }
}
