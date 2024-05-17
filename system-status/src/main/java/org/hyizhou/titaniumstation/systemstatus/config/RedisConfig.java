package org.hyizhou.titaniumstation.systemstatus.config;

import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.context.annotation.Configuration;

/**
 * 配置 redis
 * 资料：<a href="https://www.cnblogs.com/guanxiaohe/p/11949463.html">...</a>
 *
 * @author hyizhou
 * @date 2024/1/13
 */
@Configuration
public class RedisConfig extends CachingConfigurationSelector {
}
