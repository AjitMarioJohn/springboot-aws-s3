package com.demo.springboot.aws.commands.list;

import com.amazonaws.services.mq.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.demo.springboot.aws.commands.BucketCommand;
import com.demo.springboot.aws.commons.annotations.Command;
import com.demo.springboot.aws.configurations.AmazonS3Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Command
@RequiredArgsConstructor
@Slf4j
public class ListObject implements BucketCommand<Void, List<String>> {

    private final AmazonS3Configuration amazonS3Configuration;
    private final AmazonS3 amazonS3;

    @Override
    public Optional<List<String>> execute(Void unused) {
        try {
            ListObjectsRequest listObjectsRequest =
                    new ListObjectsRequest()
                            .withBucketName(amazonS3Configuration.getBucketName());
            ObjectListing bucketObjects = amazonS3.listObjects(listObjectsRequest);
            if (bucketObjects == null) {
                throw new NotFoundException("BucketObject is null");
            }
            List<String> objectKeys = new ArrayList<>();
            while(true){
                List<S3ObjectSummary> objectSummaries = bucketObjects.getObjectSummaries();
                if (objectSummaries.size() < 1) {
                    break;
                }
                List<String> keys = objectSummaries.stream().filter(summary -> !summary.getKey().endsWith("/")).map(summary -> summary.getKey())
                        .collect(Collectors.toList());
                objectKeys.addAll(keys);
                bucketObjects = amazonS3.listNextBatchOfObjects(bucketObjects);
            }
            return Optional.of(objectKeys);
        } catch (Exception exception) {
            log.error("error while listing the object in bucket ", exception);
        }

        return Optional.empty();
    }
}
