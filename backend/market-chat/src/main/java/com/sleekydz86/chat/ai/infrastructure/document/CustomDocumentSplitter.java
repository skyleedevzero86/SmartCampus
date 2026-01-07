package com.sleekydz86.chat.ai.infrastructure.document;

import com.sleekydz86.chat.ai.domain.document.DocumentSplitter;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomDocumentSplitter extends TextSplitter implements DocumentSplitter {
    
    @Override
    public List<Document> split(List<Document> documents) {
        return documents.stream()
                .flatMap(doc -> {
                    List<String> splitTexts = splitText(doc.getText());
                    return splitTexts.stream()
                            .map(text -> new Document(text, doc.getMetadata()));
                })
                .toList();
    }
    
    @Override
    protected List<String> splitText(String text) {
        String[] splitArray = text.split("\\s*\\R\\s*\\R\\S*");
        return List.of(splitArray);
    }
}

