package org.hyizhou.titaniumstation.ai.llmTools;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.function.Function;

/**
 * 读取超链接内容的工具
 * @date 2024/6/4
 */
public class LinkSummary implements Function<LinkSummary.Request, LinkSummary.Response> {
    private final TitaniumPython titaniumPython;

    public LinkSummary(TitaniumPython titaniumPython) {
        this.titaniumPython = titaniumPython;
    }

    @Override
    public Response apply(Request request) {
        TitaniumPython.Data result = titaniumPython.linkSummary(new TitaniumPython.Link(request.link));
        return new Response(result);
    }

    @JsonClassDescription("读取超链接内容")
    public record Request(
            @JsonProperty(required = true) @JsonPropertyDescription("超链接地址") String link
    ){}

    public record Response(
            TitaniumPython.Data result
    ){}

}
