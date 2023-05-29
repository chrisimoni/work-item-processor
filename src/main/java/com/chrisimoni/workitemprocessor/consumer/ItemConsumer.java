package com.chrisimoni.workitemprocessor.consumer;

import com.chrisimoni.workitemprocessor.collection.Item;
import com.chrisimoni.workitemprocessor.repository.ItemRepository;
import com.chrisimoni.workitemprocessor.service.ItemService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class ItemConsumer {
    private final ItemService itemService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemConsumer.class);
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @RabbitListener(
            queues = "${rabbitmq.queue.name}",
            concurrency = "2" // Set the desired number of concurrent consumers
    )
    public void consumeItem(Item item) {
        LOGGER.info(String.format("Consumed item -> %s", item.toString()));
        //utilizing CompletableFuture and a custom ExecutorService can help prevent thread blocks
        //and allow concurrent processing of items.
        CompletableFuture.runAsync(() -> {
            itemService.processItem(item);
        }, executorService);
    }

    @PreDestroy
    public void shutdownExecutorService() {
        executorService.shutdown();
    }

}
