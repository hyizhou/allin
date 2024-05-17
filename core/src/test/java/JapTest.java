import jakarta.annotation.Resource;
import org.hyizhou.titaniumstation.systemstatus.dao.NetworkContDailyDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author hyizhou
 * @date 2024/1/18
 */
@SpringBootTest
public class JapTest {
    @Autowired
    @Resource(name = "networkContDailyDao")
    private NetworkContDailyDao networkContDailyDao;

    @Test
    public void test(){
        if (networkContDailyDao == null) {
            System.out.println("error");
        }
    }
}
