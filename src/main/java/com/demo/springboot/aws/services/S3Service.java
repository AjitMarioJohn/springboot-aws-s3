package com.demo.springboot.aws.services;

import com.demo.springboot.aws.models.ApiResponse;
import com.demo.springboot.aws.models.ObjectUploadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {
    ResponseEntity<ApiResponse> uploadObject(MultipartFile multipartFile);
    ResponseEntity<Byte[]> download(int id);
    ResponseEntity<ApiResponse> listObjects();
}
