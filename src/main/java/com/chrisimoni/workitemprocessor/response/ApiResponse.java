package com.chrisimoni.workitemprocessor.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class ApiResponse {
   private String status;
   private Object data;
}
