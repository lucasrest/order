package br.com.mouts.order.config;

import br.com.mouts.order.adapter.ExternalAAdapter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.host}")
    private String rabbitmqHost;

    @Value("${rabbitmq.port}")
    private int rabbitmqPort;

    @Value("${rabbitmq.username}")
    private String rabbitmqUsername;

    @Value("${rabbitmq.password}")
    private String rabbitmqPassword;

    public static final String EXCHANGE_ORDER = "order.exchange";
    public static final String EXCHANGE_ORDER_PROCESSED = "order.processed.exchange";
    public static final String ROUTING_KEY = "order.routing.key";
    public static final String ROUTING_KEY_PROCESSED = "order.processed.routing.key";
    public static final String QUEUE_ORDER = "order.queue";
    public static final String QUEUE_ORDER_PROCESSED = "order.processed.queue";
    public static final String QUEUE_ORDER_PROCESSED_DLQ = "order.processed.queue.dlq";

    @Bean
    public Queue queueOrder() {
        return new Queue(QUEUE_ORDER, true, false, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_ORDER);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queueOrder()).to(exchange()).with(ROUTING_KEY);
    }

    @Bean
    public Queue queueOrderProcessed() {
        return new Queue(QUEUE_ORDER_PROCESSED, true, false, false, Map.of(
                "x-dead-letter-exchange", "",
                "x-dead-letter-routing-key", QUEUE_ORDER_PROCESSED_DLQ));
    }

    @Bean
    public Queue queueOrderProcessedDlq() {
        return new Queue(QUEUE_ORDER_PROCESSED_DLQ, true);
    }

    @Bean
    public TopicExchange exchangeProcessed() {
        return new TopicExchange(EXCHANGE_ORDER_PROCESSED);
    }

    @Bean
    public Binding bindingProcessed() {
        return BindingBuilder.bind(queueOrderProcessed()).to(exchangeProcessed()).with(ROUTING_KEY_PROCESSED);
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitmqHost);
        connectionFactory.setPort(rabbitmqPort);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public MessageListenerContainer messageListenerContainer(CachingConnectionFactory connectionFactory,
                                                             Jackson2JsonMessageConverter messageConverter,
                                                             ExternalAAdapter externalAAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(new MessageListenerAdapter(externalAAdapter, "receiveMessage"));
        container.setConcurrentConsumers(50);
        container.setMaxConcurrentConsumers(100);
        return container;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
