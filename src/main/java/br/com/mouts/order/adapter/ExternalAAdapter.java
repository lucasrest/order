package br.com.mouts.order.adapter;

import br.com.mouts.order.config.RabbitMQConfig;
import br.com.mouts.order.model.Order;
import br.com.mouts.order.service.OrderFacade;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExternalAAdapter {

    @Autowired
    private OrderFacade orderFacade;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER)
    public void receiveMessage(Order order) {
        orderFacade.createAndProcessOrder(order.getRefId(), order.getProducts());
    }

    /*@RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_PROCESSED)
    public void receiveMessageProcessed(Order order) {
        System.out.println("Ordem processada: " + order.toString());
    }*/
}
