package org.hyizhou.titaniumstation.ai.api.interfaces;

import org.hyizhou.titaniumstation.common.ai.request.AddPromptReq;

import java.util.List;

/**
 * 对 prompt 进行一些操作的 api 接口
 * @date 2024/6/11
 */
public interface IPromptApi {

    String addPrompt(AddPromptReq promptReq);

    /**
     * 查询prompt，参数可填可空，联合生效
     * @param id id
     * @param keywords 关键字查询
     * @param tags 标签
     */
    String queryPrompt(String id, String keywords, String[] tags);

    /**
     * 将获得到的 prompt 列表
     * 列表由两部分组成：属于自身私有的prompt 和 公共的 prompt
     * @param limit 防止数据量太大而做的限制数目
     */
    List<String> AllPrompt(int limit);
}
