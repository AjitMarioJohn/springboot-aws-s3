package com.demo.springboot.aws.services.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.demo.springboot.aws.commands.BucketCommand;
import com.demo.springboot.aws.commons.utils.ResponseBuilder;
import com.demo.springboot.aws.models.ApiResponse;
import com.demo.springboot.aws.models.ObjectUploadResponse;
import com.demo.springboot.aws.services.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class S3ServiceImpl implements S3Service {
    @Qualifier(value = "nonPublicBucketUpload")
    private final BucketCommand<MultipartFile, ObjectUploadResponse> uploadCommand;
    private final BucketCommand<Integer, S3Object> downloadObject;
    private final BucketCommand<Void, List<String>> listObject;

    @Override
    public ResponseEntity<ApiResponse> uploadObject(MultipartFile multipartFile) {
        Optional<ObjectUploadResponse> result = uploadCommand.execute(multipartFile);
        return result.map(response -> ResponseBuilder.buildResponse(HttpStatus.OK, "Object got uploaded to bucket", response))
                .orElse(ResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload object", null));

    }

    @Override
    public ResponseEntity<Byte[]> download(int id) {
        return (ResponseEntity<Byte[]>) downloadObject.execute(id).map(s3Object -> {
            String contentType = s3Object.getObjectMetadata().getContentType();
            byte[] bytes = new byte[0];
            try {
                bytes = s3Object.getObjectContent().readAllBytes();
            } catch (IOException e) {
                log.error("error while downloading the file ", e);
                return ResponseEntity.internalServerError().build();
            }
            return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).contentLength(bytes.length)
                    .body(bytes);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ApiResponse> listObjects() {
        Optional<List<String>> objectListOptional = listObject.execute(null);
        return objectListOptional.map(objectList -> ResponseBuilder.buildResponse(HttpStatus.OK, "List of objects in bucket", objectList))
                .orElse(ResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get list of object in bucket", null));
    }
}