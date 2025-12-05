package com.sleekydz86.server.market.board.application;


import com.sleekydz86.server.market.board.domain.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploader {

    void upload(final List<Image> images, final List<MultipartFile> fileImages);

    void delete(final List<Image> deletedImages);
}
