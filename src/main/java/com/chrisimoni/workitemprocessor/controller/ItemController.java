package com.chrisimoni.workitemprocessor.controller;

import com.chrisimoni.workitemprocessor.collection.Item;
import com.chrisimoni.workitemprocessor.dto.ItemReportDto;
import com.chrisimoni.workitemprocessor.request.ItemRequestBody;
import com.chrisimoni.workitemprocessor.response.ApiResponse;
import com.chrisimoni.workitemprocessor.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vi/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<?> createItem(@Valid @RequestBody ItemRequestBody request) {
        String id = itemService.createItem(request);
        ApiResponse response = ApiResponse.builder().status("success").data(id).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable String itemId) {
        Item item = itemService.getItem(itemId);
        ApiResponse response = ApiResponse.builder().status("success").data(item).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable String itemId) {
        itemService.deleteItem(itemId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/report")
    public ResponseEntity<?> getReport() {
        List<ItemReportDto> itemReportList = itemService.generteReport();
        ApiResponse response = ApiResponse.builder().status("success").data(itemReportList).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
