package at.sintrum.fog.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Michael Mittermayr on 16.05.2017.
 */
@Configuration
public class DockerConfig {

    public DockerConfig(@Value("${DOCKER_HOST:unix:///var/run/docker.sock}") String dockerHost) {
        this.dockerHost = dockerHost;
    }

    private final String dockerHost;

    @Bean
    public DockerClient client() {

        Logger logger = LoggerFactory.getLogger(DockerConfig.class);
        logger.info("Docker-Host: " + dockerHost);
        if (dockerHost.startsWith("unix:///") && System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            logger.warn("Docker-Host won't work on this system. Use TCP!");
        }

        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerHost)
                .withApiVersion("1.27")
                .withRegistryUrl("https://index.docker.io/v1/")
                .build();
        return DockerClientBuilder.getInstance(config).build();
    }
}
