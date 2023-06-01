package com.chrisimoni.workitemprocessor.repository;

import com.chrisimoni.workitemprocessor.collection.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    long countByValueAndProcessed(int value, boolean status);

    List<Item> findTop100ByProcessedFalse();
}
