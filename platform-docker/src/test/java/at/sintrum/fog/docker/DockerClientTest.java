package at.sintrum.fog.docker;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Michael Mittermayr on 17.05.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DockerClientTest {

    @Test()
    @Ignore
    public void testWindowsDockerConnection() {

        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {

            DockerConfig dockerConfig = new DockerConfig("tcp://127.0.0.1:2375");
//            DockerClient client = //dockerConfig.client();
//
//            assertThat(client).isNotNull();
//            List<Container> exec = client.listContainersCmd().exec();
//            assertThat(exec).isNotNull();
        }
    }
}
