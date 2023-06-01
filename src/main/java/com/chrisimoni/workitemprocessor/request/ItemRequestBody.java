package com.chrisimoni.workitemprocessor.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestBody {
    @Min(value = 1, message = "value must be greater than or equal to 1")
    @Max(value = 10, message = "value must be less than or equal to 10")
    private int value;
}
