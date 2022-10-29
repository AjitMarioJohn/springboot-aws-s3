package com.demo.springboot.aws.commands.download;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.demo.springboot.aws.commands.BucketCommand;
import com.demo.springboot.aws.commons.annotations.Command;
import com.demo.springboot.aws.models.FileMeta;
import com.demo.springboot.aws.repositories.FileMetaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
@Command
@Slf4j
@RequiredArgsConstructor
public class DownloadObject implements BucketCommand<Integer, S3Object> {
    private final FileMetaRepository fileMetaRepository;
    private final AmazonS3 amazonS3;
    @Override
    public Optional<S3Object> execute(Integer id) {
        try {
            FileMeta fileMeta = fileMetaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
            return Optional.ofNullable(amazonS3.getObject(fileMeta.getFilePath(),fileMeta.getFileName()));
        } catch (Exception exception) {
            log.error("Error occur while downloading file", exception);
        }
        return Optional.empty();
    }
}
