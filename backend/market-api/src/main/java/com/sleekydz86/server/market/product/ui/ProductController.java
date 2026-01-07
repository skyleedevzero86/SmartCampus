package com.sleekydz86.server.market.product.ui;

import com.sleekydz86.server.market.member.ui.auth.support.AuthMember;
import com.sleekydz86.server.market.product.application.ProductQueryService;
import com.sleekydz86.server.market.product.application.ProductService;
import com.sleekydz86.server.market.product.application.dto.ProductCreateRequest;
import com.sleekydz86.server.market.product.application.dto.ProductUpdateRequest;
import com.sleekydz86.server.market.product.application.dto.ProductWithImageResponse;
import com.sleekydz86.server.market.product.application.dto.UsingCouponRequest;
import com.sleekydz86.server.market.product.domain.dto.ProductPagingSimpleResponse;
import com.sleekydz86.server.market.product.ui.support.ViewCountChecker;
import com.sleekydz86.server.market.product.ai.ProductViewRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/categories")
@RestController
public class ProductController {

    private final ProductService productService;
    private final ProductQueryService productQueryService;
    private final com.sleekydz86.server.market.product.ai.ProductViewRecommendationService productViewRecommendationService;

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<ProductPagingSimpleResponse>> findAllProductsInCategory(
            @AuthMember final Long memberId,
            @PathVariable("categoryId") final Long categoryId,
            @RequestParam(name = "productId", required = false) final Long productId,
            @RequestParam(name = "pageSize") final Integer pageSize) {
        return ResponseEntity.ok(productQueryService.findAllProductsInCategory(memberId, productId, categoryId, pageSize));
    }

    @PostMapping("/{categoryId}/products")
    public ResponseEntity<Long> uploadProduct(
            @PathVariable("categoryId") final Long categoryId,
            @AuthMember final Long memberId,
            @ModelAttribute final ProductCreateRequest request
    ) {
        Long savedProductId = productService.uploadProduct(memberId, categoryId, request);
        return ResponseEntity.created(URI.create("/api/categories/" + categoryId + "/products/" + savedProductId))
                .build();
    }

    @GetMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductWithImageResponse> findProductById(
            @PathVariable("productId") final Long productId,
            @PathVariable("categoryId") final Long categoryId,
            @AuthMember final Long memberId,
            @ViewCountChecker final Boolean canAddViewCount
    ) {
        productService.addViewCount(productId, canAddViewCount);
        ProductWithImageResponse response = productQueryService.findById(productId, memberId);
        
        List<ProductPagingSimpleResponse> similarProducts = 
                productViewRecommendationService.recommendSimilarProducts(productId, memberId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{categoryId}/products/{productId}/similar")
    public ResponseEntity<List<ProductPagingSimpleResponse>> getSimilarProducts(
            @PathVariable("productId") final Long productId,
            @PathVariable("categoryId") final Long categoryId,
            @AuthMember final Long memberId
    ) {
        List<ProductPagingSimpleResponse> similarProducts = 
                productViewRecommendationService.recommendSimilarProducts(productId, memberId);
        return ResponseEntity.ok(similarProducts);
    }

    @PatchMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable("productId") final Long productId,
            @PathVariable("categoryId") final Long categoryId,
            @AuthMember final Long memberId,
            @ModelAttribute final ProductUpdateRequest request
    ) {
        productService.update(productId, memberId, request);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/{categoryId}/products/likes")
    public ResponseEntity<List<ProductPagingSimpleResponse>> findLikesProduct(
            @PathVariable("categoryId") final Long categoryId,
            @AuthMember final Long memberId
    ) {
        return ResponseEntity.ok(productQueryService.findLikesProducts(memberId));
    }

    @PatchMapping("/{categoryId}/products/{productId}/likes")
    public ResponseEntity<Boolean> likesProduct(
            @PathVariable("categoryId") final Long categoryId,
            @PathVariable("productId") final Long productId,
            @AuthMember final Long memberId
    ) {
        System.out.println("gogo " + memberId + " " + productId);
        boolean likes = productService.likes(productId, memberId);
        return ResponseEntity.ok(likes);
    }

    @DeleteMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<Long> deleteProduct(
            @PathVariable("productId") final Long productId,
            @PathVariable("categoryId") final Long categoryId,
            @AuthMember final Long memberId
    ) {
        productService.delete(productId, memberId);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<Void> buyProducts(
            @PathVariable("productId") final Long productId,
            @PathVariable("categoryId") final Long categoryId,
            @AuthMember final Long memberId,
            @RequestBody final UsingCouponRequest usingCouponRequest
    ) {
        productService.buyProducts(productId, memberId, usingCouponRequest);
        return ResponseEntity.ok().build();
    }
}
