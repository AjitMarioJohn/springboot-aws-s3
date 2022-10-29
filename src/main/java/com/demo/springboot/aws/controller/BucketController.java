package com.demo.springboot.aws.controller;

import com.demo.springboot.aws.models.ApiResponse;
import com.demo.springboot.aws.services.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/s3/bucket")
@RequiredArgsConstructor
public class BucketController {

    private final S3Service s3Service;

    @PostMapping("/uploadFile")
    public ResponseEntity<ApiResponse> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return s3Service.uploadObject(file);
    }

    @GetMapping("download/{id}")
    public ResponseEntity<Byte[]> download(@PathVariable int id) {
        return s3Service.download(id);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> listObject() {
        return s3Service.listObjects();
    }
}
