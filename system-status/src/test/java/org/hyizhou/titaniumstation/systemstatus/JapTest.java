package org.hyizhou.titaniumstation.systemstatus;

import jakarta.annotation.Resource;
import org.hyizhou.titaniumstation.systemstatus.dao.NetworkContDailyDao;
import org.hyizhou.titaniumstation.systemstatus.pojo.NetworkContDailyPojo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * @author hyizhou
 * @date 2024/1/18
 */
@SpringBootTest(
        classes = TestSystemstatusApplication.class,
        properties = "spring.config.location=classpath:application-common.yaml"
)
public class JapTest {
    @Autowired
    @Resource(name = "networkContDailyDao")
    private NetworkContDailyDao networkContDailyDao;

    @Test
    public void test(){
        if (networkContDailyDao == null) {
            System.out.println("error");
            return;
        }
        System.out.println("数据表条数："+networkContDailyDao.count());
        List<NetworkContDailyPojo> all = networkContDailyDao.findAll();
        for (NetworkContDailyPojo networkContDailyPojo : all) {
            System.out.println(networkContDailyPojo);
        }
    }

    @Test
    public void installTest(){
        NetworkContDailyPojo pojo = new NetworkContDailyPojo();
        pojo.setDate(new Date(Instant.now().toEpochMilli()));
        pojo.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        pojo.setBytesReceived(132463L);
        pojo.setBytesSent(88888811L);
        pojo.setPeriodStart(new Time(Instant.now().toEpochMilli()));
        pojo.setInterfaceName("wlan0");
        NetworkContDailyPojo save = networkContDailyDao.save(pojo);
        System.out.println(save);
    }
}
