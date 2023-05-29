package com.chrisimoni.workitemprocessor.repository;

import com.chrisimoni.workitemprocessor.collection.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {
    long countByValueAndProcessed(int value, boolean status);
}
