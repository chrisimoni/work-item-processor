package com.chrisimoni.workitemprocessor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemReportDto {
    private int value;
    private long totalItems;
    private long processedItems;
}
