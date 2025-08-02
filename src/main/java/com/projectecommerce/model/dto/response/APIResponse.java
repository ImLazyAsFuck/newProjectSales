package com.projectecommerce.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class APIResponse<T>{
    private T data;
    private String message;
    private boolean success;
    private T errors;
    private LocalDateTime timeStamp;
}
