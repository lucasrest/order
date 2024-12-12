package br.com.mouts.order.controller;

import br.com.mouts.order.model.Order;
import br.com.mouts.order.model.Product;
import br.com.mouts.order.service.OrderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderFacade orderFacade;

    @Autowired
    public OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping
    public Order createOrder(@RequestParam String refId, @RequestBody List<Product> products) {
        return orderFacade.createAndProcessOrder(refId, products);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderFacade.getOrder(id);
    }
}
