package org.hyizhou.titaniumstation.common.tools;

import java.time.Instant;

/**
 * @author hyizhou
 * @date 2024/1/15
 */
public class TimeTool {
    /**
     * 获取时间戳
     */
    public static long getTimestamp(){
          return Instant.now().toEpochMilli();
    }
}
