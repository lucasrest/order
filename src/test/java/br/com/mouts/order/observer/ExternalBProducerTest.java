package br.com.mouts.order.observer;

import br.com.mouts.order.config.RabbitMQConfig;
import br.com.mouts.order.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

public class ExternalBProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;
    @InjectMocks
    private ExternalBProducer externalBProducer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOnOrderProcessed() {
        Order order = mock(Order.class);

        externalBProducer.onOrderProcessed(order);

        verify(rabbitTemplate)
                .convertAndSend(eq(RabbitMQConfig.EXCHANGE_ORDER_PROCESSED), eq(RabbitMQConfig.ROUTING_KEY_PROCESSED), eq(order));
    }
}
