package org.hyizhou.titaniumstation.common.ai.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 消息对象
 * @date 2024/5/16
 */
public class ContentReq {
    /*
对话ID
 */
    @JsonProperty("dialog_id") String dialogId;

    /*
    消息内容，可能为文本、图片、音频、视频等，非文本类型需要使用Base64编码
     */
    @JsonProperty("content") String content;

    /*
    内容类型
     */
    @JsonProperty("type") String type;

    /*
    消息角色，比如system、user、assistant
     */
    @JsonProperty("role") String role;

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
