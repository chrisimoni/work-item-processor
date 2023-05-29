package com.chrisimoni.workitemprocessor.service;

import com.chrisimoni.workitemprocessor.collection.Item;
import com.chrisimoni.workitemprocessor.dto.ItemReportDto;
import com.chrisimoni.workitemprocessor.request.ItemRequestBody;

import java.util.List;

public interface ItemService {
    String createItem(ItemRequestBody itemRequestBody);

    Item getItem(String itemId);

    void deleteItem(String itemId);

    void processItem(Item item);

    List<ItemReportDto> generteReport();
}

