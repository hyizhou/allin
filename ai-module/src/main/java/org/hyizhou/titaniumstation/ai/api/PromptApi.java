package org.hyizhou.titaniumstation.ai.api;

import org.hyizhou.titaniumstation.ai.api.interfaces.IPromptApi;
import org.hyizhou.titaniumstation.common.ai.request.AddPromptReq;

import java.util.List;

/**
 * 操作PromptApi
 * @date 2024/6/11
 */
public class PromptApi implements IPromptApi {

    @Override
    public String addPrompt(AddPromptReq promptReq) {
        return "";
    }

    @Override
    public String queryPrompt(String id, String keywords, String[] tags) {
        return "";
    }

    @Override
    public List<String> AllPrompt(int limit) {
        return List.of();
    }
}
