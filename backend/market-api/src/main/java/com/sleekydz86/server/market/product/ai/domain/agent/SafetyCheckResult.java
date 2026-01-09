package com.sleekydz86.server.market.product.ai.domain.agent;

import java.util.List;

public record SafetyCheckResult(
        SafetyLevel level,             
        List<String> warnings,        
        List<String> recommendations,  
        String summary                
) {
    public enum SafetyLevel {
        SAFE("안전한 거래입니다"),
        CAUTION("주의가 필요합니다"),
        RISKY("위험 요소가 있습니다");
        
        private final String description;
        
        SafetyLevel(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}

