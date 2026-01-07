package com.sleekydz86.chat.ai.infrastructure.search;

import cn.hutool.json.JSONUtil;
import com.sleekydz86.chat.ai.bean.SearchEngineResponse;
import com.sleekydz86.chat.ai.domain.search.SearchResult;
import com.sleekydz86.chat.ai.domain.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchEngineService implements SearchService {
    
    @Value("${internet.websearch.searchengine.url:http://localhost:6080/search}")
    private String searchEngineUrl;
    
    @Value("${internet.websearch.searchengine.counts:25}")
    private Integer searchResultCount;
    
    private final OkHttpClient okHttpClient;
    
    @Override
    public List<SearchResult> search(String query) {
        HttpUrl url = HttpUrl.get(searchEngineUrl)
                .newBuilder()
                .addQueryParameter("q", query)
                .addQueryParameter("format", "json")
                .build();
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        log.info("검색엔진에 요청 전송 중: {}", url);
        
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = "응답 본문을 가져올 수 없음";
                try (ResponseBody body = response.body()) {
                    if (body != null) {
                        errorBody = body.string();
                    }
                } catch (IOException e) {
                    log.error("검색엔진 오류 응답 본문 읽기 실패", e);
                }
                throw new RuntimeException(String.format(
                        "검색엔진 요청 실패. 상태 코드: %d, URL: %s, 응답 본문: %s",
                        response.code(), url, errorBody
                ));
            }
            
            ResponseBody body = response.body();
            if (body != null) {
                String responseBody = body.string();
                log.debug("검색엔진 응답 내용: {}", responseBody);
                
                SearchEngineResponse searchEngineResponse = JSONUtil.toBean(responseBody, SearchEngineResponse.class);
                if (searchEngineResponse != null && searchEngineResponse.getResults() != null) {
                    return processSearchResults(searchEngineResponse.getResults());
                } else {
                    log.warn("검색엔진이 반환한 JSON을 파싱할 수 없거나 결과가 비어있음. 응답: {}", responseBody);
                    return Collections.emptyList();
                }
            }
            
        } catch (IOException e) {
            log.error("검색엔진 요청 중 네트워크 IO 예외 발생, URL: {}", url, e);
            throw new RuntimeException("검색엔진 요청 중 네트워크 IO 예외 발생", e);
        }
        
        return Collections.emptyList();
    }
    
    private List<SearchResult> processSearchResults(List<com.sleekydz86.chat.ai.bean.SearchResult> results) {
        if (results.isEmpty()) {
            return Collections.emptyList();
        }
        
        return results.stream()
                .limit(searchResultCount)
                .sorted(Comparator.comparingDouble(com.sleekydz86.chat.ai.bean.SearchResult::getScore).reversed())
                .map(this::convertToDomainResult)
                .toList();
    }
    
    private SearchResult convertToDomainResult(com.sleekydz86.chat.ai.bean.SearchResult beanResult) {
        return new SearchResult(
                beanResult.getTitle(),
                beanResult.getContent(),
                beanResult.getUrl(),
                beanResult.getScore()
        );
    }
}

