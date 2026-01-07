package com.sleekydz86.chat.ai.infrastructure.document;

import com.sleekydz86.chat.ai.domain.document.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RedisDocumentRepository implements DocumentRepository {
    
    private final RedisVectorStore redisVectorStore;
    
    @Override
    public void saveAll(List<Document> documents) {
        redisVectorStore.add(documents);
    }
    
    @Override
    public List<Document> findSimilarDocuments(String question) {
        return redisVectorStore.similaritySearch(question);
    }
}

