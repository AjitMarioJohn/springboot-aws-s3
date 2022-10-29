package com.demo.springboot.aws.commons.utils;

import com.demo.springboot.aws.models.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
public class ResponseBuilder {
    private ResponseBuilder(){}
    
    public static ResponseEntity<ApiResponse> buildResponse(HttpStatus httpStatusCode, String message, Object data){
        ApiResponse apiResponse = ApiResponse.builder().httpStatusCode(httpStatusCode.value()).message(message).data(data).build();
        return new ResponseEntity<>(apiResponse, httpStatusCode);
    }
}
