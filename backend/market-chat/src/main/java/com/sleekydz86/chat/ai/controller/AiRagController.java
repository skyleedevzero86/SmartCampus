package com.sleekydz86.chat.ai.controller;

import com.sleekydz86.chat.ai.application.document.DocumentApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai/rag")
@RequiredArgsConstructor
public class AiRagController {

    private final DocumentApplicationService documentApplicationService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        documentApplicationService.loadDocument(file.getResource(), file.getOriginalFilename());
        return ResponseEntity.ok("문서가 성공적으로 업로드되었습니다.");
    }
}

