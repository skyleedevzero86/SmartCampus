package com.sleekydz86.chat.ai.domain.document;

import org.springframework.ai.document.Document;

import java.util.List;

public interface DocumentSplitter {
    
    List<Document> split(List<Document> documents);
}

