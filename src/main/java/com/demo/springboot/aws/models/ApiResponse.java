package com.demo.springboot.aws.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;

import java.util.Map;

@JsonPropertyOrder({ "httpHeaders", "httpStatusCode", "message", "data", "otherParams" })
@Builder
@Getter
public class ApiResponse<T> {
    private final HttpHeaders httpHeaders;
    private final int httpStatusCode;
    private final String message;
    private final T data;
    private final Map<String, Object> otherParams;
}
