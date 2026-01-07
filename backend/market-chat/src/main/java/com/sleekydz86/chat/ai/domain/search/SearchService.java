package com.sleekydz86.chat.ai.domain.search;

import java.util.List;

public interface SearchService {
    
    List<SearchResult> search(String query);
}

