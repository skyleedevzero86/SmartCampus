package com.sleekydz86.server.market.board.infrastructure;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sleekydz86.server.global.exception.exceptions.FileUploadFailureException;
import com.sleekydz86.server.market.board.application.ImageUploader;
import com.sleekydz86.server.market.board.domain.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageUploaderImpl implements ImageUploader {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public void upload(final List<Image> images, final List<MultipartFile> fileImages) {
        IntStream.range(0, images.size())
                .forEach(index -> saveFile(
                        fileImages.get(index),
                        images.get(index).getUniqueName()
                ));
    }

    private void saveFile(final MultipartFile file, final String fileUniqueName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            amazonS3Client.putObject(bucket, fileUniqueName, file.getInputStream(), metadata);
            log.info("파일 저장 성공 : " + bucket + "/" + fileUniqueName);
        } catch (IOException e) {
            log.info("파일 저장 실패 : " + bucket + " " + fileUniqueName);
            throw new FileUploadFailureException(e);
        }
    }

    @Override
    public void delete(final List<Image> deletedImages) {
        deletedImages.forEach(image -> deleteImage(image.getUniqueName()));
    }

    private void deleteImage(final String fileUrl) {
        String[] urlParts = fileUrl.split("/");
        String objectKey = urlParts.length > 3 
            ? String.join("/", Arrays.copyOfRange(urlParts, 3, urlParts.length))
            : fileUrl;

        try {
            amazonS3Client.deleteObject(bucket, objectKey);
            log.info("파일 삭제 완료: " + objectKey);
        } catch (AmazonS3Exception e) {
            log.error("파일 삭제 실패: " + e.getMessage());
            throw new FileUploadFailureException(e.getCause());
        } catch (SdkClientException e) {
            log.error("AWS SDK 클라이언트 오류: " + e.getMessage());
            throw new FileUploadFailureException(e.getCause());
        }
    }
}
