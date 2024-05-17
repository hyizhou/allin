package org.hyizhou.titaniumstation.core.controller

import com.fasterxml.jackson.core.type.TypeReference
import jakarta.servlet.http.HttpSession
import org.hyizhou.titaniumstation.common.service.RedisService
import org.hyizhou.titaniumstation.common.tools.SimpleJsonTool
import org.hyizhou.titaniumstation.core.service.SystemStatusService
import org.hyizhou.titaniumstation.systemstatus.config.StatusModelKey
import org.hyizhou.titaniumstation.systemstatus.model.NetSpeedModel
import org.hyizhou.titaniumstation.systemstatus.model.NetworkCountModel
import org.hyizhou.titaniumstation.systemstatus.model.NetworkModel
import org.hyizhou.titaniumstation.systemstatus.service.HardwareFactory
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.slf4j.Logger

@RestController
@RequestMapping("/api/status")
class SystemStatusController(
    val redisService: RedisService,
    val hardwareFactory: HardwareFactory,
    val systemStatusService: SystemStatusService
) {
    private val logger: Logger = LoggerFactory.getLogger(SystemStatusController::class.java)
    private final val timeInterval = "timeInterval"
    /**
     * 设置时间间隔，表显示多久的数据
     */
    @PutMapping("/time-interval")
    fun putTimeInterval(session: HttpSession, @RequestParam("sec") sec: Long){
        session.setAttribute(timeInterval, sec)
    }

    /**
     * 获取网速状态。
     * 返回数据条数根据session中参数控制
     */
    @GetMapping("/netspeed")
    fun getNetSpeed(session: HttpSession) : List<List<NetSpeedModel>> {
        var timeInterval = session.getAttribute(timeInterval)
        if (timeInterval == null){
            logger.debug("session中时间间隔无数据")
            timeInterval = 60L  // 相当于默认显示一分钟的数据
        }else{
            logger.debug("session中设置的时间间隔为{}秒", timeInterval)
        }

        val list = redisService.listRange(StatusModelKey.SPEED.name, 0, timeInterval as Long -1)
        val container = mutableListOf<List<NetSpeedModel>>()
        for (modelJson in list) {
            val model = SimpleJsonTool.toObject(modelJson, object : TypeReference<List<NetSpeedModel>>() {})
            container.add(model)
        }
        return container
    }

    /**
     * 获取网口列表
     */
    @GetMapping("/network-list")
    fun getNetworkList() : List<NetworkModel> {
        logger.debug("调用了getNetworkList")
        return hardwareFactory.networkModelList
    }

    /**
     * 获得流量统计
     */
    @GetMapping("/net-count")
    fun getNetCount(@RequestParam("size", required=false) size: Long?): List<List<NetworkCountModel>> {
        return if (size == null) {
            systemStatusService.getNetCount()
        }else{
            systemStatusService.getNetCount(size)
        }
    }
}