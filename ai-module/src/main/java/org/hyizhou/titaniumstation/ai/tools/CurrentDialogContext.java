package org.hyizhou.titaniumstation.ai.tools;

import org.hyizhou.titaniumstation.ai.entity.DialogEntity;

/**
 * 存储当前线程（或者说请求）的 Dialog（会话实体类）
 * @date 2024/5/17
 */
public class CurrentDialogContext {
    private static final ThreadLocal<DialogEntity> threadLocal = new ThreadLocal<>();

    public static void set(DialogEntity content) {
        threadLocal.set(content);
    }

    public static DialogEntity get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }


}
