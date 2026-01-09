package com.sleekydz86.server.market.product.application;

import com.sleekydz86.server.market.product.domain.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageUploadPort {

    void upload(final List<ProductImage> images, final List<MultipartFile> fileImages);

    void delete(final List<ProductImage> deletedImages);
}




