package br.com.mouts.order.observer;

import br.com.mouts.order.config.RabbitMQConfig;
import br.com.mouts.order.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExternalBProducer implements OrderObserver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void onOrderProcessed(Order order) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ORDER_PROCESSED, RabbitMQConfig.ROUTING_KEY_PROCESSED, order);
    }
}
