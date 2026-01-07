package com.sleekydz86.chat.ai.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchEngineResponse {

    private String query;

    private List<SearchResult> results;

}

