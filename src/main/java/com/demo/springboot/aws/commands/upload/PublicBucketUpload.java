package com.demo.springboot.aws.commands.upload;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.demo.springboot.aws.commands.BucketCommand;
import com.demo.springboot.aws.commons.annotations.Command;
import com.demo.springboot.aws.commons.utils.AwsS3Utils;
import com.demo.springboot.aws.configurations.AmazonS3Configuration;
import com.demo.springboot.aws.models.ObjectUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

/**
 * This class upload object in bucket whose "Block all public access" is unchecked
 */
@Command
@RequiredArgsConstructor
public final class PublicBucketUpload implements BucketCommand<MultipartFile, ObjectUploadResponse> {
    private final AmazonS3Configuration amazonS3Configuration;
    private final AmazonS3 s3client;

    @Override
    public Optional<ObjectUploadResponse> execute(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = AwsS3Utils.convertMultiPartToFile(multipartFile);
            String fileName = AwsS3Utils.generateFileName(multipartFile);
            fileUrl = amazonS3Configuration.getEndpointUrl() + "/" + amazonS3Configuration.getBucketName() + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
            return Optional.ofNullable(new ObjectUploadResponse(fileName, fileUrl,null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        //we are adding PublicRead permissions to this file. It means that anyone who have the file url can access this file
        s3client.putObject(new PutObjectRequest(amazonS3Configuration.getBucketName(), fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }
}
