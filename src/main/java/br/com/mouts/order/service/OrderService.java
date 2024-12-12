package br.com.mouts.order.service;

import br.com.mouts.order.exception.NotFoundException;
import br.com.mouts.order.model.Order;
import br.com.mouts.order.observer.ExternalBProducer;
import br.com.mouts.order.observer.OrderObserver;
import br.com.mouts.order.repository.OrderRepository;
import br.com.mouts.order.utils.OrderStatusEnum;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final List<OrderObserver> observers = new ArrayList<>();

    @Autowired
    private OrderCalculator orderCalculator;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ExternalBProducer externalBProducer;

    @PostConstruct
    public void init() {
        observers.add(externalBProducer);
    }

    public Order createOrder(Order order) {
        Optional<Order> existingOrder = orderRepository.findByRefId(order.getRefId());
        return existingOrder.orElseGet(() -> orderRepository.save(order));
    }

    public Order processOrder(Long orderId) {
        Order order = getOrder(orderId);
        if (OrderStatusEnum.PROCESSADO.equals(order.getStatus())) {
            return order;
        }

        order.setStatus(OrderStatusEnum.PROCESSADO);
        order.setTotalAmount(orderCalculator.calculateTotal(order));
        orderRepository.save(order);
        notifyObservers(order);
        return order;
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));
    }

    private void notifyObservers(Order order) {
        for (OrderObserver observer : observers) {
            observer.onOrderProcessed(order);
        }
    }
}
