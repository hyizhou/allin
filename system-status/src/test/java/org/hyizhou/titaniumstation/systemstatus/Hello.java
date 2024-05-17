package org.hyizhou.titaniumstation.systemstatus;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.VirtualMemory;

/**
 * @author hyizhou
 * @date 2024/1/14
 */
public class Hello {
    private static HardwareAbstractionLayer hardware;
    @BeforeAll
    public static void create(){
        SystemInfo systemInfo = new SystemInfo();
        hardware = systemInfo.getHardware();
    }

    @Test
    public void test1(){
        GlobalMemory memory = hardware.getMemory();

        long total = memory.getTotal();  // 总内存
        long available = memory.getAvailable();  // 可用物理内存数
        VirtualMemory swap = memory.getVirtualMemory();  // 虚拟内存对象
        long swapTotal = swap.getSwapTotal();
        long swapUsed = swap.getSwapUsed();
        // MemoryModel memoryModel = new MemoryModel(total, available, swapTotal, swapTotal - swapUsed);
        // System.out.println(memoryModel);
    }
}
