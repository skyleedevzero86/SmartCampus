package com.sleekydz86.server.market.product.ai.domain.agent;

public record DescriptionImprovementResult(
        String improvedDescription,   
        String originalDescription,   
        List<String> improvements,     
        String reasoning               
) {
}

