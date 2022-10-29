package com.demo.springboot.aws.commands.upload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.demo.springboot.aws.commands.BucketCommand;
import com.demo.springboot.aws.commons.annotations.Command;
import com.demo.springboot.aws.configurations.AmazonS3Configuration;
import com.demo.springboot.aws.models.FileMeta;
import com.demo.springboot.aws.models.ObjectUploadResponse;
import com.demo.springboot.aws.repositories.FileMetaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Command
@Slf4j
@RequiredArgsConstructor
@Primary
public final class NonPublicBucketUpload implements BucketCommand<MultipartFile, ObjectUploadResponse> {
    private final AmazonS3 amazonS3;
    private final AmazonS3Configuration amazonS3Configuration;
    private final FileMetaRepository fileMetaRepository;
    @Override
    public Optional<ObjectUploadResponse> execute(MultipartFile file){
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));

            String path = String.format("%s/%s", amazonS3Configuration.getBucketName(), UUID.randomUUID());
            String fileName = String.format("%s", file.getOriginalFilename());
            PutObjectResult putObjectResult = upload(
                    path, fileName, Optional.of(metadata), file.getInputStream());
            fileMetaRepository.save(new FileMeta(fileName, path, putObjectResult.getMetadata().getVersionId()));
            return Optional.ofNullable(new ObjectUploadResponse(fileName, null, putObjectResult.getMetadata().getVersionId()));
        } catch (Exception exception) {
            log.error("Error while uploading file to S3 ", exception);
        }
        return Optional.empty();
    }

    public PutObjectResult upload(
            String path,
            String fileName,
            Optional<Map<String, String>> optionalMetaData,
            InputStream inputStream) {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        log.debug("Path: " + path + ", FileName:" + fileName);
        return amazonS3.putObject(path, fileName, inputStream, objectMetadata);
    }
}
