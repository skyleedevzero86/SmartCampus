package com.sleekydz86.server.market.product.infrastructure;

import com.sleekydz86.server.global.exception.exceptions.FileUploadFailureException;
import com.sleekydz86.server.market.product.application.ProductImageUploadPort;
import com.sleekydz86.server.market.product.domain.ProductImage;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@Profile("local")
public class ProductImageUploadAdapter implements ProductImageUploadPort {

    @Value("${file.upload.location}")
    private String location;

    @PostConstruct
    void createSavedDir() {
        File dir = new File(location);
        createDir(dir);
    }

    private void createDir(final File dir) {
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    @Override
    public void upload(final List<ProductImage> images, final List<MultipartFile> fileImages) {
        IntStream.range(0, images.size())
                .forEach(index -> saveFile(
                        fileImages.get(index),
                        images.get(index).getUniqueName()
                ));
    }

    private void saveFile(final MultipartFile file, final String fileUniqueName) {
        try {
            file.transferTo(new File(location + fileUniqueName));
            log.info("파일 저장 성공 : " + location + fileUniqueName);
        } catch (IOException e) {
            log.info("파일 저장 실패 : " + location + fileUniqueName);
            throw new FileUploadFailureException(e);
        }
    }

    @Override
    public void delete(final List<ProductImage> deletedImages) {
        deletedImages.forEach(image -> deleteImage(image.getUniqueName()));
    }

    private void deleteImage(final String fileUniqueName) {
        new File(location + fileUniqueName).delete();
    }
}

