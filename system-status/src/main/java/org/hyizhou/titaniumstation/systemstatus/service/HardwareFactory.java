package org.hyizhou.titaniumstation.systemstatus.service;

import org.hyizhou.titaniumstation.common.tools.TimeTool;
import org.hyizhou.titaniumstation.systemstatus.model.MemoryModel;
import org.hyizhou.titaniumstation.systemstatus.model.NetworkModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.VirtualMemory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hyizhou
 * @date 2024/1/14
 */
@Component
public class HardwareFactory {
    private final Logger logger = LoggerFactory.getLogger(HardwareFactory.class);
    private final HardwareAbstractionLayer hardware;

    public HardwareFactory(){
        SystemInfo systemInfo = new SystemInfo();
        this.hardware = systemInfo.getHardware();
    }

    /**
     * 获取最新的内存状态模型
     */
    public MemoryModel getNewMemoryModel(){
        GlobalMemory memory = hardware.getMemory();
        long total = memory.getTotal();  // 总内存
        long available = memory.getAvailable();  // 可用物理内存数
        VirtualMemory swap = memory.getVirtualMemory();  // 虚拟内存对象
        long swapTotal = swap.getSwapTotal();
        long swapUsed = swap.getSwapUsed();
        return MemoryModel.builder().totalMemory(total).freeMemory(available).totalSwap(swapTotal).freeSwap(swapTotal - swapUsed).timestamp(TimeTool.getTimestamp()).build();
    }

    /**
     * 获取网络接口对象模型
     * @return 多个网口组成的列表
     */
    public List<NetworkModel> getNetworkModelList(){
        List<NetworkModel> container = new ArrayList<>();
        // 剔除虚拟接口
        List<NetworkIF> networkIFs = hardware.getNetworkIFs(false);
        long timestamp = TimeTool.getTimestamp();
        for (NetworkIF networkIF : networkIFs) {
            String name = networkIF.getName();
            // 排除docker创建的一些虚拟接口
            if (name.startsWith("br-") || name.startsWith("veth") || name.startsWith("docker")){
                continue;
            }
            NetworkModel model = NetworkModel.builder()
                    .name(networkIF.getName())
                    .timestamp(timestamp)
                    .bytesRecv(networkIF.getBytesRecv())
                    .bytesSent(networkIF.getBytesSent())
                    .build();
            container.add(model);
        }
        return container;
    }

}
