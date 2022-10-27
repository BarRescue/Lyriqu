package com.lyriqu.subscription.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class DirectConfiguration {

    @Bean
    public Queue createCustomerQueue() {
        return new Queue("createCustomer", true);
    }

    @Bean
    public Queue getCustomerQueue() {
        return new Queue("getCustomer", true);
    }

    @Bean
    public Queue addMandateQueue() {
        return new Queue("addMandate", true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("subscription-exchange");
    }

    @Bean
    public Binding createCustomerBinding(Queue createCustomerQueue, DirectExchange exchange) {
        return BindingBuilder.bind(createCustomerQueue).to(exchange).with("create-customer");
    }

    @Bean
    public Binding getCustomerBinding(Queue getCustomerQueue, DirectExchange exchange) {
        return BindingBuilder.bind(getCustomerQueue).to(exchange).with("get-customer");
    }

    @Bean
    public Binding addMandateBinding(Queue addMandateQueue, DirectExchange exchange) {
        return BindingBuilder.bind(addMandateQueue).to(exchange).with("add-mandate");
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        // Uncomment for Production
        factory.setHost("hello-world.default.svc.cluster.local");

        // Uncomment for test
        //factory.setHost("localhost");

        factory.setUsername("cUr8rHbO9CQ3Bx8VhDWcm7gW0H3MIQGt");
        factory.setPassword("7sHfEEVzqeJZvKDmP5RnXZ_MzJq0cHTy");

        //factory.setUsername("guest");
        //factory.setPassword("guest");
        factory.setPort(5672);
        return factory;
    }

    @Bean
    public AmqpAdmin rabbitAdmin() {
        AmqpAdmin amqpAdmin = new RabbitAdmin(connectionFactory());
        amqpAdmin.declareQueue(createCustomerQueue());
        amqpAdmin.declareQueue(getCustomerQueue());
        amqpAdmin.declareQueue(addMandateQueue());

        return amqpAdmin;
    }
}
