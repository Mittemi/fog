package at.sintrum.fog.redis;

import at.sintrum.fog.redis.model.TestModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Michael Mittermayr on 26.07.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
public class JodaTest {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testInit() {
        assertThat(redissonClient).isNotNull();
    }


    @Test
    public void testDate() {
        RList<TestModel> list = redissonClient.getList("TestList.Joda");
        list.clear();
        list.add(new TestModel(DateTime.now()));

        List<TestModel> testModels = list.readAll();

        assertThat(testModels.size()).isGreaterThanOrEqualTo(1);
        assertThat(testModels.get(0)).isNotNull();
        assertThat(testModels.get(0).getSomeTime()).isNotNull();

        list.remove(testModels.get(0));
    }
}
