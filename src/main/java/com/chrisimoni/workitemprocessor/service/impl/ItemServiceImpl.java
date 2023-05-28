package com.chrisimoni.workitemprocessor.service.impl;

import com.chrisimoni.workitemprocessor.collection.Item;
import com.chrisimoni.workitemprocessor.exceptions.BadRequestException;
import com.chrisimoni.workitemprocessor.exceptions.NotFoundException;
import com.chrisimoni.workitemprocessor.repository.ItemRepository;
import com.chrisimoni.workitemprocessor.request.ItemRequestBody;
import com.chrisimoni.workitemprocessor.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public String createItem(ItemRequestBody itemRequestObj) {
        Item item = Item.builder()
                .value(itemRequestObj.getValue())
                .processed(false)
                .result(null)
                .build();

        return itemRepository.save(item).getId();
    }

    @Override
    public Item getItem(String itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if(!item.isPresent()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, String.format("No item found with id %s", itemId));
        }

        return item.get();
    }

    @Override
    public void deleteItem(String itemId) {
        Optional<Item> itemObj = itemRepository.findById(itemId);
        if(!itemObj.isPresent()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, String.format("No item found with id %s", itemId));
        }

        Item item = itemObj.get();
        if(item.isProcessed()) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Item is already processed and cannot be deleted");
        }

        itemRepository.delete(item);
    }
}
