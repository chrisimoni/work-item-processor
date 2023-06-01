package com.chrisimoni.workitemprocessor.service;

import com.chrisimoni.workitemprocessor.collection.Item;
import com.chrisimoni.workitemprocessor.dto.ItemReportDto;
import com.chrisimoni.workitemprocessor.exceptions.BadRequestException;
import com.chrisimoni.workitemprocessor.exceptions.NotFoundException;
import com.chrisimoni.workitemprocessor.producer.ItemProducer;
import com.chrisimoni.workitemprocessor.repository.ItemRepository;
import com.chrisimoni.workitemprocessor.request.ItemRequestBody;
import com.chrisimoni.workitemprocessor.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Test
    void testCreateItem_ShouldCreateItem() {
        ItemRequestBody itemRequestObj = ItemRequestBody.builder().value(5).build();
        Item createdItem = new Item("1234566", 5, false, null);

        when(itemRepository.save(any(Item.class))).thenReturn(createdItem);

        String itemId = itemService.createItem(itemRequestObj);

        assertEquals(createdItem.getId(), itemId);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testGetItem_ShouldReturnExistingItem() {
        String itemId = "1234567";
        Item item = new Item(itemId, 3, false, null);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Item result = itemService.getItem(itemId);

        assertNotNull(result);
        assertEquals(item, result);
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void testGetItem_NonExistingItemId_ShouldThrowNotFoundException() {
        String itemId = "2345678";

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItem(itemId));
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void testDeleteItem_ShouldDeleteExistingAndUnprocessedItem() {
        // Arrange
        String itemId = "3456789";
        Item item = new Item(itemId, 3, false, null);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        itemService.deleteItem(itemId);

        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).delete(item);
    }

    @Test
    void testDeleteItem_ExistingAndProcessedItem_ShouldThrowBadRequestException() {
        String itemId = "2345678";
        Item item = new Item(itemId, 7, true, 49);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // Assert
        assertThrows(BadRequestException.class, () -> itemService.deleteItem(itemId));
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, never()).delete(any(Item.class));
    }

    @Test
    void testProcessItem_ShouldUpdateItem() {
        Item item = new Item("1234566", 3, false, null);

        itemService.processItem(item);

        assertTrue(item.isProcessed());
        assertNotNull(item.getResult());
        assertEquals(item.getResult(), item.getValue() * item.getValue());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void generteReport_ShouldReturnItemReportList() {
        // Arrange
        List<Item> allItems = new ArrayList<>();
        allItems.add(new Item("123454", 7, true, 49));
        allItems.add(new Item("123455", 7, true, 49));
        allItems.add(new Item("123456", 3, false, null));
        allItems.add(new Item("123458", 2, true, 2));
        allItems.add(new Item("123459", 2, false, null));

        when(itemRepository.findAll()).thenReturn(allItems);
        when(itemRepository.countByValueAndProcessed(7, true)).thenReturn(2L);
        when(itemRepository.countByValueAndProcessed(2, true)).thenReturn(1L);
        when(itemRepository.countByValueAndProcessed(3, true)).thenReturn(0L);

        // Act
        List<ItemReportDto> report = itemService.generteReport();

        assertNotNull(report);
        assertEquals(3, report.size());

        verify(itemRepository, times(1)).findAll();
        verify(itemRepository, times(1)).countByValueAndProcessed(7, true);
        verify(itemRepository, times(1)).countByValueAndProcessed(2, true);
    }
}
