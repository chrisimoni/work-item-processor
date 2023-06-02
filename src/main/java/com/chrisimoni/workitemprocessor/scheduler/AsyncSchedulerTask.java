package com.chrisimoni.workitemprocessor.scheduler;

import com.chrisimoni.workitemprocessor.collection.Item;
import com.chrisimoni.workitemprocessor.producer.ItemProducer;
import com.chrisimoni.workitemprocessor.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class AsyncSchedulerTask {
    private final ItemRepository itemRepository;
    private final ItemProducer itemProducer;

    @Scheduled(fixedRate = 60000) // Runs every 60 seconds (1 minute)
    public void scheduleItemsForProcessing() {
        List<Item> items = itemRepository.findTop100ByProcessedFalse();
        if(items.isEmpty()) {
            return;
        }

        for (Item item : items) {
            itemProducer.enqueueItem(item);
        }
    }

    @Async
    public void generateAndProccessItems() {
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            int value = generateRandomNumber(1, 10);
            Item item = Item.builder()
                    .value(value)
                    .processed(false)
                    .result(null)
                    .build();

            items.add(item);
        }

        itemRepository.saveAll(items);
    }

    private static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
