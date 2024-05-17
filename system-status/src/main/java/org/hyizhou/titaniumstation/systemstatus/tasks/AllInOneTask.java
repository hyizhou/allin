package org.hyizhou.titaniumstation.systemstatus.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import org.hyizhou.titaniumstation.common.service.RedisService;
import org.hyizhou.titaniumstation.common.tools.SimpleJsonTool;
import org.hyizhou.titaniumstation.common.tools.TimeTool;
import org.hyizhou.titaniumstation.systemstatus.config.DefaultConfigs;
import org.hyizhou.titaniumstation.systemstatus.config.StatusModelKey;
import org.hyizhou.titaniumstation.systemstatus.dao.NetworkContDailyDao;
import org.hyizhou.titaniumstation.systemstatus.dao.NetworkContHourlyDao;
import org.hyizhou.titaniumstation.systemstatus.model.MemoryModel;
import org.hyizhou.titaniumstation.systemstatus.model.NetSpeedModel;
import org.hyizhou.titaniumstation.systemstatus.model.NetworkCountModel;
import org.hyizhou.titaniumstation.systemstatus.model.NetworkModel;
import org.hyizhou.titaniumstation.systemstatus.pojo.NetworkContDailyPojo;
import org.hyizhou.titaniumstation.systemstatus.pojo.NetworkContHourlyPojo;
import org.hyizhou.titaniumstation.systemstatus.service.HardwareFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 定时任务
 *
 * @author hyizhou
 * @date 2024/1/11
 */
@Component
public class AllInOneTask {
    private final Logger log = LoggerFactory.getLogger(AllInOneTask.class);
    private final HardwareFactory hardwareFactory;
    private final RedisService redis;
    private final NetworkContDailyDao networkContDailyDao;
    @Autowired
    private final NetworkContHourlyDao networkContHourlyDao;
    // 用于计算实时网速，存储上一秒的网口数据
    private List<NetworkModel> lastNetworkModelList;
    // 用于统计网络流量，一般是一分钟前的内容
    private List<NetworkModel> networkSnapshotListMinAgo;
    // 用于统计每日网络流量，此处存储一般是一天前的网口数据
    private List<NetworkModel> networkSnapshotListDayAgo;
    // 用于统计每小时网络流量，此处存储一小时前的网口数据
    private List<NetworkModel> networkSnapshotListHourAgo;

    @PostConstruct
    private void init(){
        List<NetworkModel> netSnapshot = hardwareFactory.getNetworkModelList();
        networkSnapshotListDayAgo = netSnapshot;
        networkSnapshotListHourAgo = netSnapshot;
    }


    public AllInOneTask(HardwareFactory hardwareFactory, RedisService redisService, NetworkContDailyDao networkContDailyDao, NetworkContHourlyDao networkContHourlyDao){
        this.hardwareFactory = hardwareFactory;
        this.redis = redisService;
        this.networkContDailyDao = networkContDailyDao;
        this.networkContHourlyDao = networkContHourlyDao;
        // 利用构造函数实现系统启动时记录一次数据
//        List<NetworkModel> netSnapshot = hardwareFactory.getNetworkModelList();
//        networkSnapshotListDayAgo = netSnapshot;
//        networkSnapshotListHourAgo = netSnapshot;
    }

    // 系统启动延迟5秒后启动任务，一秒执行一次，记录内存变化
    @Scheduled(initialDelay = 5000, fixedRate = 1000)
    public void updateMemoryStatus() throws JsonProcessingException {
        // ObjectMapper
        MemoryModel memoryModel = hardwareFactory.getNewMemoryModel();
        redis.leftPush(StatusModelKey.MEM.name(), SimpleJsonTool.toString(memoryModel), DefaultConfigs.MEN_CACHE_MAX_SIZE);
    }

    // 更新网速
    @Scheduled(initialDelay = 5000, fixedRate = 1000)
    public void updateNetSpeedStatus() throws JsonProcessingException {
        List<NetSpeedModel> netSpeedModelList = new ArrayList<>();
        List<NetworkModel> networkModelList = hardwareFactory.getNetworkModelList();
        if (this.lastNetworkModelList == null){
            this.lastNetworkModelList = networkModelList;
            return;
        }
        for (NetworkModel networkModel : networkModelList) {
            String name = networkModel.getName();
            Optional<NetworkModel> lastModelOptional = lastNetworkModelList.stream()
                    .filter(obj -> name.equals(obj.getName()))
                    .findFirst();
            lastModelOptional.ifPresent(
                    lastNetworkModel -> {
                        NetSpeedModel netSpeedModel = NetSpeedModel.builder()
                                .name(name)
                                .sentSpeed(networkModel.getBytesSent() - lastNetworkModel.getBytesSent())
                                .recvSpeed(networkModel.getBytesRecv() - lastNetworkModel.getBytesRecv())
                                .timestamp(TimeTool.getTimestamp()).build();
                        netSpeedModelList.add(netSpeedModel);
                    }
            );
        }
        this.lastNetworkModelList = networkModelList;
        redis.leftPush(StatusModelKey.SPEED.name(), netSpeedModelList, DefaultConfigs.NET_SPEED_NAX_SIZE);
    }

    // 六十秒统计计算一次流量使用
    @Scheduled(initialDelay = 5000, fixedRate = 60000)
    public void countNetwork() throws JsonProcessingException {
        List<NetworkModel> snapshotList = hardwareFactory.getNetworkModelList();
        List<NetworkCountModel> countModelList = new ArrayList<>();
        if (this.networkSnapshotListMinAgo == null){
            this.networkSnapshotListMinAgo = snapshotList;
            return;
        }
        for (NetworkModel lastSnapshot : networkSnapshotListMinAgo) {
            String name = lastSnapshot.getName();
            NetworkModel snapshot;
            Optional<NetworkModel> optional = snapshotList.stream().filter(obj -> name.equals(obj.getName())).findFirst();
            if (optional.isEmpty()) {
                continue;
            }else {
                snapshot = optional.get();
            }
            NetworkCountModel countModel = NetworkCountModel.builder().timestamp(TimeTool.getTimestamp())
                    .startTime(lastSnapshot.getTimestamp())
                    .endTime(snapshot.getTimestamp())
                    .name(name)
                    .totalSent(snapshot.getBytesSent() - lastSnapshot.getBytesSent())
                    .totalRecv(snapshot.getBytesRecv() - lastSnapshot.getBytesRecv())
                    .build();
            countModelList.add(countModel);
        }
        redis.leftPush(
                StatusModelKey.NETCOUNT.name(),
                countModelList,
                DefaultConfigs.NET_COUNT_MAX_SIZE
        );
    }

    /**
     * 统计每天的流量，统计时间0点1分或系统启动时间到次日0点1分
     */
    @Scheduled(cron = "0 1 0 * * ?")
    @Transactional
    public void countNetDaily(){
        log.debug("countNetDaily 开始...");
        List<NetworkModel> nowNetList = hardwareFactory.getNetworkModelList();
        Map<String, NetworkModel> beforeNetMap = networkSnapshotListDayAgo.stream().collect(Collectors.toMap(NetworkModel::getName, element -> element));
        Map<String, NetworkModel> nowNetMap = nowNetList.stream().collect(Collectors.toMap(NetworkModel::getName, element -> element));
        nowNetMap.entrySet().stream()
                .filter(entry -> beforeNetMap.containsKey(entry.getKey()))
                .forEach(entry -> {
                    NetworkModel beforeEntry = beforeNetMap.get(entry.getKey());
                    NetworkModel nowEntry = entry.getValue();
                    NetworkContDailyPojo pojo = new NetworkContDailyPojo();
                    pojo.setInterfaceName(nowEntry.getName());
                    pojo.setPeriodStart(new Time(beforeEntry.getTimestamp()));
                    pojo.setDate(new Date(Instant.now().toEpochMilli()));
                    pojo.setCreatedAt(new Timestamp(Instant.now().toEpochMilli()));
                    pojo.setBytesSent(nowEntry.getBytesSent() - beforeEntry.getBytesSent());
                    pojo.setBytesReceived(nowEntry.getBytesRecv() - beforeEntry.getBytesRecv());
                    networkContDailyDao.save(pojo);
                });
        networkSnapshotListDayAgo = nowNetList;
        log.debug("countNetDaily 结束...");
    }

    /**
     * 统计每小时流量
     */
    @Scheduled(cron = "5 0 * * * ?")
    @Transactional
    public void countNetHourly(){
        log.debug("countNetHourly 开始...");
        List<NetworkModel> nowNetList = hardwareFactory.getNetworkModelList();
        Map<String, NetworkModel> beforeNetMap = networkSnapshotListHourAgo.stream().collect(Collectors.toMap(NetworkModel::getName, element -> element));
        Map<String, NetworkModel> nowNetMap = nowNetList.stream().collect(Collectors.toMap(NetworkModel::getName, element -> element));
        nowNetMap.entrySet().stream()
                .filter(entry -> beforeNetMap.containsKey(entry.getKey()))
                .forEach(entry -> {
                    NetworkModel beforeEntry = beforeNetMap.get(entry.getKey());
                    NetworkModel nowEntry = entry.getValue();
                    NetworkContHourlyPojo pojo = new NetworkContHourlyPojo();
                    pojo.setInterfaceName(nowEntry.getName());
                    pojo.setHour(new Timestamp(Instant.now().truncatedTo(ChronoUnit.HOURS).toEpochMilli()));
                    pojo.setCreatedAt(new Timestamp(Instant.now().toEpochMilli()));
                    pojo.setBytesSent(nowEntry.getBytesSent() - beforeEntry.getBytesSent());
                    pojo.setBytesReceived(nowEntry.getBytesRecv() - beforeEntry.getBytesRecv());
                    networkContHourlyDao.save(pojo);
                });
        networkSnapshotListHourAgo = nowNetList;
        log.debug("countNetHourly 结束...");
    }

}
