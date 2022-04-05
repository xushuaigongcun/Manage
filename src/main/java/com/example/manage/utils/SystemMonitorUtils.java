package com.example.manage.utils;


import ch.qos.logback.core.util.SystemInfo;
import com.example.manage.vo.MonitorVo;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;


/**
 * @author xushuai
 * @date 2022年04月04日 18:41
 */
@Slf4j
public class SystemMonitorUtils {
    private static SystemInfo systemInfo = new SystemInfo();
    private static MonitorVo monitorVo = new MonitorVo();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DecimalFormat decimalFormat = new DecimalFormat(".00");

    static {
        monitorVo.setCpuInfo(System.getProperties().getProperty("os.name"));
        monitorVo.setJvmJavaVersion(System.getProperty("java.version"));
        monitorVo.setRunTime(simpleDateFormat.format(ManagementFactory.getRuntimeMXBean().getStartTime()));
        //monitorVo.setCpuInfo(System.getProperties().);
    }

        public static MonitorVo getSysMonitor(){
            //jvm
            MemoryUsage heapInfo = getHeapInfo();
            monitorVo.setJvmHeapInit(decimalFormat.format(heapInfo.getInit() / 1024 / 1024));
            monitorVo.setJvmHeapMax(decimalFormat.format(heapInfo.getMax() / 1024 / 1024));
            monitorVo.setJvmHeapUsed(decimalFormat.format(heapInfo.getUsed() / 1024 / 1024));
            monitorVo.setJvmHeapCommitted(decimalFormat.format(heapInfo.getCommitted() / 1024 / 1024));
            MemoryUsage noHeapInfo = getNoHeapInfo();
            monitorVo.setJvmNonHeapInit(decimalFormat.format(noHeapInfo.getInit() / 1024 / 1024));
            monitorVo.setJvmNonHeapMax(decimalFormat.format(noHeapInfo.getMax() / 1024 / 1024));
            monitorVo.setJvmNonHeapUsed(decimalFormat.format(noHeapInfo.getUsed() / 1024 / 1024));
            monitorVo.setJvmNonHeapCommitted(decimalFormat.format(noHeapInfo.getCommitted() / 1024 / 1024));

            //系统信息
            monitorVo.setCpuUseRate(decimalFormat.format(getCpuUsage() * 100));
            OperatingSystemMXBean memoryUsage = getMemoryUsage();
//            monitorVo.setRamTotal(decimalFormat.format(memoryUsage.getTotalPhysicalMemorySize() / 1024 / 1024 / 1024));
//            monitorVo.setRamUsed(decimalFormat.format((memoryUsage.getTotalPhysicalMemorySize() - memoryUsage.getFreePhysicalMemorySize()) / 1024 / 1024 / 1024));
          HashMap<String, Double> diskUsage = getDiskUsage();
            monitorVo.setDiskTotal(decimalFormat.format(diskUsage.get("total")));
            monitorVo.setDiskUsed(decimalFormat.format(diskUsage.get("used")));
            return monitorVo;

    }

    /**
     * 获取jvm中的堆信息
     * @author xushuai
     * @date 2022/4/4 19:30
     * @return java.lang.management.MemoryUsage
     */
    private static MemoryUsage getHeapInfo(){
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    }
    /**
     * 获取jvm中非堆内存信息
     * @author xushuai
     * @date 2022/4/4 19:31
     * @param
     * @return null
     */
    private static MemoryUsage getNoHeapInfo(){
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
    }
    /**
     * 获取内存信息
     * @author xushuai
     * @date 2022/4/4 19:34
     * @return java.lang.management.OperatingSystemMXBean
     */
    private static OperatingSystemMXBean getMemoryUsage(){
        return (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }
    /**
     * 获取CPU信息
     * @author xushuai
     * @date 2022/4/4 19:34
     * @param
     * @return null
     */
    private static double getCpuUsage(){
        // return systemInfo.getHardware().getProcessor().getSystemCpuLoadBetweenTicks();
        return 0;
    }

    /**
     * 判断系统是否为windows
     *
     * @return 是否
     */
    private static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS");
    }
    /**
     * 获取磁盘信息
     */
    private static HashMap<String,Double>  getDiskUsage(){
        HashMap<String, Double> hashMap = new HashMap<>(2);
        File[] files = File.listRoots();
        double total = 0;
        double used = 0;
        for (File file : files) {
            total = total +file.getTotalSpace()/1024/1024/1024;
            used = used +file.getFreeSpace()/1024/1024/1024;
        }
        hashMap.put("total",total);
        hashMap.put("used",used);
        return hashMap;
    }
    /**
     * 获取linux 磁盘使用率
     *
     * @return 磁盘使用率
     */
    private static HashMap<String, Long> getUnixDiskUsage(){
        HashMap<String, Long> stringLongHashMap = new HashMap<>(2);
        String ioCmdStr = "df -h /";
        String info= runCommand(ioCmdStr);
        log.info(info);
        String[] split = info.split(" +");
        double total = Double.parseDouble(split[9].replace("%", ""));
        return stringLongHashMap;

    }

    /**
     * Linux 执行系统命令
     *
     * @param cmd 命令
     * @return 字符串结果
     */
    private static String runCommand(String cmd){
        StringBuilder info = new StringBuilder(50);
        InputStreamReader isr = null;
        LineNumberReader lnr = null;
        try {
            Process pos = Runtime.getRuntime().exec(cmd);
            pos.waitFor();
            isr = new InputStreamReader(pos.getInputStream());
            lnr = new LineNumberReader(isr);
        } catch (IOException e) {
            log.error("输出有误，请检查程序");
        } catch (InterruptedException e) {
            log.error("输出有误，请检查程序");
        }finally {
            try {
                if (isr != null){
                    isr.close();
                }
                if (lnr != null){
                    lnr.close();
                }
            }catch (IOException e){
                log.error("输出有误，请检查程序");
            }
        }
        return info.toString();
    }

    /**
     * 获取Windows 磁盘使用率
     *
     * @return 磁盘使用率
     */
    private static HashMap<String, Long> getWinDiskUsage() {
//        HardwareAbstractionLayer hal = systemInfo.getHardware();
//        HWDiskStore[] diskStores = hal.getDiskStores();
        HashMap<String, Long> hashMap = new HashMap<>(2);
        long total = 0;
        long used = 0;
//        if (diskStores != null && diskStores.length > 0) {
//            for (HWDiskStore diskStore : diskStores) {
//                long size = diskStore.getSize();
//                long writeBytes = diskStore.getWriteBytes();
//                total += size;
//                used += writeBytes;
//            }
//        }
        hashMap.put("total",total);
        hashMap.put("used",used);
        return hashMap;
    }


}
