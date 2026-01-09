package com.sleekydz86.server.market.product.ai.domain.agent;

public record ProductConditionResult(
        ConditionLevel level,      
        String description,       
        String reasoning,         
        List<String> suggestions  
) {
    public enum ConditionLevel {
        NEW("새상품"),
        LIKE_NEW("거의 새것"),
        GOOD("양호"),
        FAIR("보통"),
        POOR("사용감 있음");
        
        private final String description;
        
        ConditionLevel(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}

