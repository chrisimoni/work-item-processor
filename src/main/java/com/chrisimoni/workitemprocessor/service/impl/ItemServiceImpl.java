package com.chrisimoni.workitemprocessor.service.impl;

import com.chrisimoni.workitemprocessor.collection.Item;
import com.chrisimoni.workitemprocessor.dto.ItemReportDto;
import com.chrisimoni.workitemprocessor.exceptions.BadRequestException;
import com.chrisimoni.workitemprocessor.exceptions.NotFoundException;
import com.chrisimoni.workitemprocessor.producer.ItemProducer;
import com.chrisimoni.workitemprocessor.repository.ItemRepository;
import com.chrisimoni.workitemprocessor.request.ItemRequestBody;
import com.chrisimoni.workitemprocessor.scheduler.AsyncSchedulerTask;
import com.chrisimoni.workitemprocessor.service.ItemService;
import com.chrisimoni.workitemprocessor.util.ReportUtil;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemProducer itemProducer;
    private final AsyncSchedulerTask asyncSchedulerTask;

    @Override
    public String createItem(ItemRequestBody itemRequestObj) {
        Item item = Item.builder()
                .value(itemRequestObj.getValue())
                .processed(false)
                .result(null)
                .build();

        Item createdItem = itemRepository.save(item);

        return createdItem.getId();
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

    @Override
    public void processItem(Item item) {
        // Simulated delay
        try {
            Thread.sleep(item.getValue() * 10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        item.setProcessed(true);
        item.setResult(item.getValue() * item.getValue());
        itemRepository.save(item);

    }

    @Override
    public List<ItemReportDto> generteReport() {
        List<Item> allItems = itemRepository.findAll();
        //group items by their value
        Map<Integer, Long> itemCounts = allItems.stream()
                .collect(Collectors.groupingBy(Item::getValue, Collectors.counting()));

        List<ItemReportDto> itemReportList = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : itemCounts.entrySet()) {
            int value = entry.getKey();
            long totalItems = entry.getValue();
            long processedItems = itemRepository.countByValueAndProcessed(value, true);

            ItemReportDto itemReport = ItemReportDto.builder()
                    .value(value)
                    .totalItems(totalItems)
                    .processedItems(processedItems)
                    .build();

            itemReportList.add(itemReport);
        }

        return itemReportList;
    }

    @Override
    public void generteItems() {
        asyncSchedulerTask.generateAndProccessItems();
    }

    @Override
    public byte[] generateReportToDownload() throws JRException {
        // Generate user data
        List<ItemReportDto> items = generteReport();
        byte[] pdfBytes = ReportUtil.generateJasperReport(items);

        return pdfBytes;
    }




    /*private JasperDesign createReportTemplate() throws JRException {
        // Create a new JasperDesign
        JasperDesign jasperDesign = new JasperDesign();

        // Set properties of the JasperDesign
        jasperDesign.setName("ItemReport");
        jasperDesign.setPageWidth(595); // Set the page width in pixels
        jasperDesign.setPageHeight(842); // Set the page height in pixels
        jasperDesign.setColumnWidth(555); // Set the column width in pixels



        // Create a detail band
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(30); // Set the height as needed

        // Create text fields in the detail band for displaying item data
        JRDesignTextField valueTextField = new JRDesignTextField();
        valueTextField.setExpression(new JRDesignExpression("$F{value}")); // Set the field expression
        valueTextField.setX(10); // Set the x-coordinate position
        valueTextField.setY(10); // Set the y-coordinate position
        valueTextField.setWidth(100); // Set the width as needed
        valueTextField.setHeight(20); // Set the height as needed
        detailBand.addElement(valueTextField);

        // Set the detail section to the JasperDesign
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);
        // Save the JasperDesign as JRXML file
//        String jrxmlFile = "path/to/reportTemplate.jrxml";
//        JasperCompileManager.writeReportToXmlFile(jasperDesign, jrxmlFile);

        return jasperDesign;
    }*/


}
