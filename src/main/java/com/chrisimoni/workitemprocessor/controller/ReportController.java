package com.chrisimoni.workitemprocessor.controller;

import com.chrisimoni.workitemprocessor.service.ItemService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ItemService itemService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/report/download")
    public ResponseEntity<ByteArrayResource> downloadReport() throws IOException, JRException {
        // Generate the report
        byte[] reportBytes = itemService.generateReportToDownload();

        // Create a ByteArrayResource from the report bytes
        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        // Set the content type and headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ItemReport.pdf");

        // Return the response entity with the report bytes and headers
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(reportBytes.length)
                .body(resource);
    }
}
