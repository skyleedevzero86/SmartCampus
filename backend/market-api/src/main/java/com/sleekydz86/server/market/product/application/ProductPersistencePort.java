package com.sleekydz86.server.market.product.application;

import com.sleekydz86.server.market.product.domain.Product;

import java.util.Optional;

public interface ProductPersistencePort {

    Product save(final Product product);

    Optional<Product> findById(final Long productId);

    Optional<Product> findByIdWithPessimisticLock(final Long productId);

    void deleteProductById(final Long productId);
}



