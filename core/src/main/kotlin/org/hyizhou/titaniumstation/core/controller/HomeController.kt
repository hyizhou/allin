package org.hyizhou.titaniumstation.core.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
    @GetMapping(path = ["/hello", "/"])
    fun hello(): String{
        return "Hello Kotlin"
    }

    @GetMapping(value = ["/file"])
    fun file():String {
        return "Hello world"
    }

    /**
     * 每日一句
     */
    @GetMapping("/home/daily-quote")
    fun dailyQuote(): String{
        return "今天是昨天的明天"
    }
}
