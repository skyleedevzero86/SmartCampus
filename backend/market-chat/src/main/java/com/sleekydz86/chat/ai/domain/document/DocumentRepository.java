package com.sleekydz86.chat.ai.domain.document;

import org.springframework.ai.document.Document;

import java.util.List;

public interface DocumentRepository {

    void saveAll(List<Document> documents);

    List<Document> findSimilarDocuments(String question);
}

