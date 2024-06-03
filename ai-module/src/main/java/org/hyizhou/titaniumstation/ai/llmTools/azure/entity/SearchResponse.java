package org.hyizhou.titaniumstation.ai.llmTools.azure.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * bing搜索结果实体类
 * @date 2024/6/2
 */
@Data
public class SearchResponse {

    @JsonProperty("_type")
    private String type;
    private QueryContext queryContext;

    private WebPages webPages;

    private RankingResponse rankingResponse;

    @Data
    public static class QueryContext {
        @JsonProperty("originalQuery")
        private String originalQuery;
    }

    @Data
    public static class WebPages {
        @JsonProperty("webSearchUrl")
        private String webSearchUrl;

        @JsonProperty("totalEstimatedMatches")
        private int totalEstimatedMatches;

        @JsonProperty("value")
        private List<WebPage> value;
    }

    @Data
    public static class WebPage {
        @JsonProperty("id")
        private String id;

        private String name;

        private String url;

        private String datePublished;

        @JsonProperty("datePublishedFreshnessText")
        private String datePublishedFreshnessText;

        private boolean isFamilyFriendly;

        @JsonProperty("displayUrl")
        private String displayUrl;

        private String snippet;

        private List<DeepLink> deepLinks;

        @JsonProperty("dateLastCrawled")
        private String dateLastCrawled;

        @JsonProperty("cachedPageUrl")
        private String cachedPageUrl;

        @JsonProperty("language")
        private String language;

        @JsonProperty("isNavigational")
        private boolean isNavigational;

        @JsonProperty("noCache")
        private boolean noCache;
    }

    @Data
    public static class DeepLink {
        private String name;
        private String url;
        private String snippet;
    }

    @Data
    public static class RankingResponse {
        private Mainline mainline;
    }

    @Data
    public static class Mainline {
        private List<Item> items;
    }

    @Data
    public static class Item {
        @JsonProperty("answerType")
        private String answerType;

        @JsonProperty("resultIndex")
        private int resultIndex;

        @JsonProperty("value")
        private Value value;
    }

    @Data
    public static class Value {
        @JsonProperty("id")
        private String id;
    }
}
