package com.sleekydz86.chat.ai.application.document;

import com.sleekydz86.chat.ai.domain.document.DocumentRepository;
import com.sleekydz86.chat.ai.domain.document.DocumentSplitter;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentApplicationService {
    
    private final DocumentRepository documentRepository;
    private final DocumentSplitter documentSplitter;
    
    public void loadDocument(Resource resource, String fileName) {
        TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("fileName", fileName);
        List<Document> documents = textReader.get();
        
        List<Document> splitDocuments = documentSplitter.split(documents);
        
        documentRepository.saveAll(splitDocuments);
    }
}

