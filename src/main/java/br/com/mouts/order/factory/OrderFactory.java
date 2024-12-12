package br.com.mouts.order.factory;

import br.com.mouts.order.model.Order;
import br.com.mouts.order.model.Product;
import br.com.mouts.order.utils.OrderStatusEnum;

import java.util.List;

public class OrderFactory {

    public static Order createOrder(String refId, List<Product> products) {
        Order order = new Order();
        order.setRefId(refId);
        order.setProducts(products);
        order.setStatus(OrderStatusEnum.PENDENTE);
        return order;
    }
}
