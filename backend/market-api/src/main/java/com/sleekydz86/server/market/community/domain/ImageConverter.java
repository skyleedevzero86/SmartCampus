package com.sleekydz86.server.market.community.domain;

import com.sleekydz86.server.market.board.domain.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageConverter {

    List<Image> convertImageFilesToImages(final List<MultipartFile> imageFiles);

    List<Image> convertImageIdsToImages(final List<Long> imageIds, final List<Image> images);
}
