package com.demo.springboot.aws.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObjectUploadResponse {
    private String fileName;
    private String imageUrl;
    private String imageId;
}
