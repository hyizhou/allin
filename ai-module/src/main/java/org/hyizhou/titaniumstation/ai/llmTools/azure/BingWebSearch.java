package org.hyizhou.titaniumstation.ai.llmTools.azure;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.hyizhou.titaniumstation.ai.llmTools.azure.entity.SearchResponse;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * bing wen 搜索，通过 azure 提供的服务调用
 * TODO 直接将搜索结果返回，所包含的有用信息较少，后续应进一步读取链接中内容再做返回
 * @date 2024/5/24
 */
public class BingWebSearch implements Function<BingWebSearch.Request, BingWebSearch.Response> {
    // Enter a valid subscription key.
    private final String subscriptionKey;

    @JsonClassDescription("通过搜索引擎进行在线搜索")
    public record Request(
            @JsonProperty(required = true) @JsonPropertyDescription("搜索关键词") String q
    ) {}
    public record Response(String result) {}

    public BingWebSearch(String key){
        this.subscriptionKey = key;
    }

    @Override
    public Response apply(Request request) {
        String result = ModelOptionsUtils.toJsonString(searchWeb(request.q).getWebPages().getValue());
        return new Response(result);
    }


    /*
     * If you encounter unexpected authorization errors, double-check these values
     * against the endpoint for your Bing Web search instance in your Azure
     * dashboard.
     */
    String host = "https://api.bing.microsoft.com";
    String path = "/v7.0/search";
    WebClient webClient = WebClient.builder().baseUrl(host + path).build();

    private SearchResponse searchWeb(String searchQuery) {
        // TODO 搜索参数还需进一步优化
        return webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder
                            .queryParam("q", searchQuery)
                            .queryParam("count", 5)
                            .build();
                })
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("Ocp-Apim-Subscription-Key", subscriptionKey)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("Error response from server"))
                )
                .bodyToMono(SearchResponse.class)
                .block();
    }
}
