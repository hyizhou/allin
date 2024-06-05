package org.hyizhou.titaniumstation.ai.llmTools.azure;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.hyizhou.titaniumstation.ai.llmTools.TitaniumPython;
import org.hyizhou.titaniumstation.ai.llmTools.azure.entity.SearchResponse;
import org.hyizhou.titaniumstation.ai.tools.JsonTools;
import org.hyizhou.titaniumstation.ai.tools.TokenTools;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * bing wen 搜索，通过 azure 提供的服务调用
 * TODO 直接将搜索结果返回，所包含的有用信息较少，后续应进一步读取链接中内容再做返回
 * @date 2024/5/24
 */
@Log4j2
public class BingWebSearch implements Function<BingWebSearch.Request, BingWebSearch.Response> {
    // Enter a valid subscription key.
    private final String subscriptionKey;
    /*
    搜索返回结果条数
     */
    private final int QUERY_COUNT = 5;
    /*
    打开链接，总结内容
     */
    private final boolean openLink;
    /*
    链接总结api
     */
    private final TitaniumPython titaniumPython;

    @JsonClassDescription("通过搜索引擎进行在线搜索")
    public record Request(
            @JsonProperty(required = true) @JsonPropertyDescription("搜索关键词") String q
    ) {}
    public record Response(String result) {}

    public BingWebSearch(String key, Boolean openLink, TitaniumPython titaniumPython){
        this.subscriptionKey = key;
        this.openLink = openLink;
        this.titaniumPython = titaniumPython;
    }

    @Override
    public Response apply(Request request) {
        log.debug("调用bing搜索关键词：{}", request.q);
        SearchResponse response = searchWeb(request.q);
        if (openLink){
            Response resp = new Response(getUrlSummary(response));
            log.debug("\n{}", resp);
            return resp;
        }else {
            List<SearchResponse.WebPage> webPageList = response.getWebPages().getValue();
            webPageList = webPageList.stream().peek(webPage -> {
                // 删除无用信息减少token消耗
                webPage.setCachedPageUrl(null);
                webPage.setId(null);
                webPage.setDisplayUrl(null);
                webPage.setDateLastCrawled(null);
            }).toList();
            Response resp = new Response(JsonTools.toJsonString(webPageList));
            log.debug("\n{}", resp.result);
            return resp;
        }
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
        return webClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder
                            .queryParam("q", searchQuery)
                            .queryParam("count", QUERY_COUNT)
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

    /**
     * 获取链接总结
     * @param response 搜索结果
     */
    private String getUrlSummary(SearchResponse response){
        // 显示总结能最多只能占用的token数，后续可改成自由修改的值
        int limitTotalTokenCount = 4000;
        // 平均一下每个链接消耗的token数
        int limitTokenCount = limitTotalTokenCount / QUERY_COUNT;
        List<Result> results = new ArrayList<>();
        List<SearchResponse.WebPage> webPageList = response.getWebPages().getValue();
        List<String> urls = webPageList.stream().map(SearchResponse.WebPage::getUrl).toList();
        List<TitaniumPython.Data> data = titaniumPython.urlSummary(new TitaniumPython.Urls(urls));
        for (TitaniumPython.Data d : data) {
            Result result = new Result();
            result.setUrl(d.url());
            result.setSummary(TokenTools.subString(d.summary(), 0, limitTokenCount));
            results.add(result);
        }
        return JsonTools.toJsonString(results);  // 这里得增加规则，不能传入太长的内容
    }


    @Data
    static class Result {
        private String url;
        private String summary;
    }
}
