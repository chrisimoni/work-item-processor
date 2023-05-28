package com.chrisimoni.workitemprocessor.service;

import com.chrisimoni.workitemprocessor.collection.Item;
import com.chrisimoni.workitemprocessor.request.ItemRequestBody;

public interface ItemService {
    String createItem(ItemRequestBody itemRequestBody);

    Item getItem(String itemId);

    void deleteItem(String itemId);
}

