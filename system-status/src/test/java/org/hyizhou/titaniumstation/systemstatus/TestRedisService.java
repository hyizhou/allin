package org.hyizhou.titaniumstation.systemstatus;

import org.hyizhou.titaniumstation.common.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

/**
 * @author hyizhou
 * @date 2024/1/14
 */
@SpringBootTest(
        properties = "spring.config.location=classpath:application-common.yaml"
)
public class TestRedisService {
    @Autowired
    private RedisService redisService;


    @Test
    public void test1(){
        redisService.set("冬日", "冬天的天总同时灰的，地总是白的");
    }

    @Test
    public void testGet(){
        System.out.println(redisService.get("冬日"));
    }

    @Test
    public void testLeftPush(){
        Random random = new Random();
        for (int i = 0; i < 3000; i++) {
            String s = String.valueOf(random.nextDouble());
            redisService.leftPush("test-list", s, 2000);
        }

    }
}
