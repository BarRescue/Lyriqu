package com.lyriqu.music.rabbitmq;

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
    public Queue publishReviewedQueue() {
        return new Queue("publishReviewed", true);
    }

    @Bean
    public Queue musicInfoQueue() {
        return new Queue("musicInfo", true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("music-exchange", true, false);
    }

    @Bean
    public Binding getMusicBinding(Queue musicInfoQueue, DirectExchange exchange) {
        return BindingBuilder.bind(musicInfoQueue).to(exchange).with("music-info");
    }

    @Bean
    public Binding publishReviewedBinding(Queue publishReviewedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(publishReviewedQueue).to(exchange).with("publish-reviewed");
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
        amqpAdmin.declareQueue(musicInfoQueue());
        amqpAdmin.declareQueue(publishReviewedQueue());
        return amqpAdmin;
    }
}

