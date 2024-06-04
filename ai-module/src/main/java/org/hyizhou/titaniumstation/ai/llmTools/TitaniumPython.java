package org.hyizhou.titaniumstation.ai.llmTools;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 调用python的一些服务
 * @date 2024/6/4
 */
@Component
@FeignClient(value = "titaniumPython")
public interface TitaniumPython {

    @PostMapping("/url_summary")
    List<Data> urlSummary(Urls urls);

    @PostMapping("/link_summary")
    Data linkSummary(Link link);


    record Data(
            String url,
            String text,
            String summary
    ) {}

    record Urls(
            List<String> url
    ) {}

    record Link(
            String link
    ){}
}
