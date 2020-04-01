package cn.worth.sys.pojo;

/**
 * @author myireland
 * @version 1.0.0
 * @date 2019-10-08
 * @description
 **/
//@Getter
//public class ServerInfo {
//
//    private static final int OSHI_WAIT_SECOND = 1000;
//
//    private CpuInfo cpuInfo = new CpuInfo();
//    /**
//     * 內存相关信息
//     */
//    private MemInfo memInfo = new MemInfo();
//
//    /**
//     * JVM相关信息
//     */
//    private JvmInfo jvmInfo = new JvmInfo();
//
//    /**
//     * 服务器相关信息
//     */
////    private SysInfo sysInfo = new SysInfo();
//
//    /**
//     * 磁盘相关信息
//     */
//    private List<SysFile> sysFiles = new LinkedList<SysFile>();
//
//    public ServerInfo() {
//        SystemInfo si = new SystemInfo();
//        HardwareAbstractionLayer hal = si.getHardware();
//
//        setCpuInfo(hal.getProcessor());
//
//        setMemInfo(hal.getMemory());
//
//        setSysInfo();
//
//        setJvmInfo();
//
//        setSysFiles(si.getOperatingSystem());
//    }
//
//    private void setSysFiles(OperatingSystem operatingSystem) {
//        FileSystem fileSystem = operatingSystem.getFileSystem();
//        OSFileStore[] fsArray = fileSystem.getFileStores();
//        for (OSFileStore fs : fsArray)
//        {
//            long free = fs.getUsableSpace();
//            long total = fs.getTotalSpace();
//            long used = total - free;
//            SysFile sysFile = new SysFile();
//            sysFile.setDirName(fs.getMount());
//            sysFile.setSysTypeName(fs.getType());
//            sysFile.setTypeName(fs.getName());
//            sysFile.setTotal(convertFileSize(total));
//            sysFile.setFree(convertFileSize(free));
//            sysFile.setUsed(convertFileSize(used));
//            sysFile.setUsage(Arith.mul(Arith.div(used, total, 4), 100));
//            sysFiles.add(sysFile);
//        }
//    }
//
//    private String convertFileSize(long size) {
//        long kb = 1024;
//        long mb = kb * 1024;
//        long gb = mb * 1024;
//        if (size >= gb) {
//            return String.format("%.1f GB", (float) size / gb);
//        }
//        else if (size >= mb) {
//            float f = (float) size / mb;
//            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
//        }
//        else if (size >= kb) {
//            float f = (float) size / kb;
//            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
//        }
//        else {
//            return String.format("%d B", size);
//        }
//    }
//
//    private void setJvmInfo() {
//        Properties props = System.getProperties();
//        jvmInfo.setTotal(Runtime.getRuntime().totalMemory());
//        jvmInfo.setMax(Runtime.getRuntime().maxMemory());
//        jvmInfo.setFree(Runtime.getRuntime().freeMemory());
//        jvmInfo.setVersion(props.getProperty("java.version"));
////        jvmInfo.setHome(props.getProperty("java.home"));
//
//    }
//
//    private void setSysInfo() {
////        Properties props = System.getProperties();
////        sysInfo.setComputerName(IPUtils.getHostName());
////        sysInfo.setComputerIp(IPUtils.getHostIp());
////        sysInfo.setOsName(props.getProperty("os.name"));
////        sysInfo.setOsArch(props.getProperty("os.arch"));
////        sysInfo.setUserDir(props.getProperty("user.dir"));
//    }
//
//    private void setMemInfo(GlobalMemory memory) {
//        memInfo.setTotal(memory.getTotal());
//        memInfo.setUsed(memory.getTotal() - memory.getAvailable());
//        memInfo.setFree(memory.getAvailable());
//    }
//
//    private void setCpuInfo(CentralProcessor processor) {
//        // CPU信息
//        long[] prevTicks = processor.getSystemCpuLoadTicks();
//        Util.sleep(OSHI_WAIT_SECOND);
//        long[] ticks = processor.getSystemCpuLoadTicks();
//        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
//        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
//        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
//        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
//        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
//        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
//        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
//        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
//        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
//        cpuInfo.setCpuNum(processor.getLogicalProcessorCount());
//        cpuInfo.setTotal(totalCpu);
//        cpuInfo.setSys(cSys);
//        cpuInfo.setUsed(user);
//        cpuInfo.setWait(iowait);
//        cpuInfo.setFree(idle);
//    }
//
//}
