package at.sintrum.fog.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Michael Mittermayr on 16.05.2017.
 */
@Configuration
public class DockerConfig {

    @Value("${DOCKER_HOST:unix:///var/run/docker.sock}")
    private String dockerHost;

    @Bean
    public DockerClient client() {

        LoggerFactory.getLogger(DockerConfig.class).info("Docker-Host: " + dockerHost);

        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerHost)
                .withApiVersion("1.27")
                .withRegistryUrl("https://index.docker.io/v1/")
                .build();
        return DockerClientBuilder.getInstance(config).build();
    }
}
