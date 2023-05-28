package com.chrisimoni.workitemprocessor.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "items")
public class Item {
    @Id
    private String id;
    private int value;
    private boolean processed;
    private Integer result;
}
