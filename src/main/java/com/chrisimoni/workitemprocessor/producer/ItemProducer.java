package com.chrisimoni.workitemprocessor.producer;

import com.chrisimoni.workitemprocessor.collection.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class ItemProducer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    private static  final Logger LOGGER = LoggerFactory.getLogger(ItemProducer.class);

    public void enqueueItem(Item item) {
        LOGGER.info(String.format("item published -> %s", item.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, item);
    }
}
