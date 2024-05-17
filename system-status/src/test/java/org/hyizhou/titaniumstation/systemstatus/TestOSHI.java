package org.hyizhou.titaniumstation.systemstatus;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.util.List;

/**
 * @author hyizhou
 * @date 2024/1/15
 */

@SpringBootTest(
        properties = "spring.config.location=classpath:application-common.yaml"
)
@EnableScheduling
public class TestOSHI {
    private static HardwareAbstractionLayer hardware;


    @BeforeAll
    public static void before(){
        SystemInfo systemInfo = new SystemInfo();
        hardware = systemInfo.getHardware();
    }

    @Test
    public void testNet() throws InterruptedException {
        List<NetworkIF> networkIFs = hardware.getNetworkIFs();
        System.out.println("网口数：" + networkIFs.size());
        networkIFs = hardware.getNetworkIFs(false);
        long recv = 0;
        long sent = 0;
        while (true) {
            // networkIFs = hardware.getNetworkIFs(false);
            for (NetworkIF network : networkIFs) {
                if (network.getName().equals("wlan4")) {
                    System.out.println(network.getDisplayName() + " --- " + network.getName() + "---" + network.getBytesRecv() + "---" + network.getBytesSent());
                    System.out.println("网速：" + (network.getBytesRecv() - recv) + "---" + (network.getBytesSent() - sent) );
                    System.out.println("测试的输出："+ network.getTimeStamp());
                    recv = network.getBytesRecv();
                    sent = network.getBytesSent();
                    network.updateAttributes();
                }
            }
            Thread.sleep(1000);
        }

    }
}
