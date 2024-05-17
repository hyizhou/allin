package org.hyizhou.titaniumstation.core.service

import com.fasterxml.jackson.core.type.TypeReference
import org.hyizhou.titaniumstation.common.service.RedisService
import org.hyizhou.titaniumstation.common.tools.SimpleJsonTool
import org.hyizhou.titaniumstation.systemstatus.config.StatusModelKey
import org.hyizhou.titaniumstation.systemstatus.model.NetworkCountModel
import org.hyizhou.titaniumstation.systemstatus.service.HardwareFactory
import org.springframework.stereotype.Service

@Service
class SystemStatusService(
    val redisService: RedisService,
    val hardwareFactory: HardwareFactory
) {
    private final val defaultNetCountSize = 20L

    /**
     * 获得每分钟流量统计
     */
    fun getNetCount(size: Long = -1L): List<List<NetworkCountModel>> {
        val container = mutableListOf<List<NetworkCountModel>>()
        val listRange = redisService.listRange(
            StatusModelKey.NETCOUNT.name,
            0,
            if(size <= -1L) defaultNetCountSize -1 else size
        )
        for (s in listRange) {
            container.add(
                SimpleJsonTool.toObject(s, object : TypeReference<List<NetworkCountModel>>() {})
            )
        }
        return container

    }
}