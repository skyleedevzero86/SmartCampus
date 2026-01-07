package com.sleekydz86.server.market.product.ai.domain.search;

import java.util.List;

public interface SearchService {
    List<SearchResult> search(String query);
}

