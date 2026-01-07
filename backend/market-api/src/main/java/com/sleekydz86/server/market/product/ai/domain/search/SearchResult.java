package com.sleekydz86.server.market.product.ai.domain.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {
    private String title;
    private String content;
    private String url;
    private Double score;
}

