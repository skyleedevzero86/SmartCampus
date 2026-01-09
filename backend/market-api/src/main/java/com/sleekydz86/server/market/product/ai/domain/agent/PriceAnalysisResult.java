package com.sleekydz86.server.market.product.ai.domain.agent;

public record PriceAnalysisResult(
        String analysis,           
        PriceLevel priceLevel,    
        Integer suggestedPrice,   
        String reasoning          
) {
    public enum PriceLevel {
        TOO_HIGH("가격이 시장 가격보다 높습니다"),
        HIGH("가격이 다소 높습니다"),
        FAIR("적정한 가격입니다"),
        LOW("가격이 다소 낮습니다"),
        TOO_LOW("가격이 시장 가격보다 낮습니다 (주의 필요)");
        
        private final String description;
        
        PriceLevel(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}

