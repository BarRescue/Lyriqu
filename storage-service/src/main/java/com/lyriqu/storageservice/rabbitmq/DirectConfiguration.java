package com.lyriqu.storageservice.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class DirectConfiguration {

    @Bean
    public Queue uploadStorageQueue() {
        return new Queue("uploadFile", true);
    }

    @Bean
    public Queue publishStorageQueue() {
        return new Queue("publishSong", true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("storage-exchange", true, false);
    }

    @Bean
    public Binding uploadStorageBinding(Queue uploadStorageQueue, DirectExchange exchange) {
        return BindingBuilder.bind(uploadStorageQueue).to(exchange).with("upload-storage");
    }

    @Bean
    public Binding publishStorageBinding(Queue publishStorageQueue, DirectExchange exchange) {
        return BindingBuilder.bind(publishStorageQueue).to(exchange).with("publish-storage");
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
        amqpAdmin.declareQueue(uploadStorageQueue());
        amqpAdmin.declareQueue(publishStorageQueue());

        return amqpAdmin;
    }
}
