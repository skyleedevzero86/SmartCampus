package com.sleekydz86.server.market.product.ai.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {
    private String title;
    private String content;
    private String url;
    private Double score;
}

