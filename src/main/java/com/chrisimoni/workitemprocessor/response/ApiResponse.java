package com.chrisimoni.workitemprocessor.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
   private String status;
   private Object data;
   private String message;
}
