package org.hyizhou.titaniumstation.systemstatus;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * @author hyizhou
 * @date 2024/1/11
 */
public class Hello {
    public static void hi(){
        System.out.println("Hello world");
    }

    public static void main(String[] args) {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hardware = si.getHardware();
//        CentralProcessor processor = hardware.getProcessor();
//        System.out.println(processor.getMaxFreq());
        System.out.println("--------------------------");
        System.out.println(hardware.getSensors().getCpuTemperature());
        System.out.println(hardware.getSensors().getCpuVoltage());
        System.out.println(hardware.getSensors().getFanSpeeds());
        System.out.println("--------------------------");
    }
}
