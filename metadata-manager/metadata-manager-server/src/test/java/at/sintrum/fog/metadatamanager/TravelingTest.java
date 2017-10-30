package at.sintrum.fog.metadatamanager;

import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.metadatamanager.api.dto.*;
import at.sintrum.fog.metadatamanager.config.MetadataManagerConfigProperties;
import at.sintrum.fog.metadatamanager.service.ContainerMetadataService;
import at.sintrum.fog.metadatamanager.service.ImageMetadataService;
import at.sintrum.fog.metadatamanager.service.requests.AppRequestServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by Michael Mittermayr on 12.10.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
public class TravelingTest {

    @Autowired
    private RedissonClient redissonClient;

    @Mock
    private EnvironmentInfoService environmentInfoService;

    @Mock
    private ContainerMetadataService containerMetadataService;

    @Mock
    private ImageMetadataService imageMetadataService;

    private FogIdentification targetA = new FogIdentification("a", 0);
    private FogIdentification targetB = new FogIdentification("b", 0);

    private final String unitTestInstanceId = "MetadataInstanceTestId";
    private String instanceId = "12345";


    @Before
    public void initMocks() {
        Mockito.when(environmentInfoService.getInstanceId()).thenReturn(unitTestInstanceId);
        Mockito.when(environmentInfoService.getApplicationName()).thenReturn("MetadataTest");

        DockerImageMetadata imageMetadata = new DockerImageMetadata();
        imageMetadata.setApplicationName(unitTestInstanceId);
        imageMetadata.setId("123");
        Mockito.when(imageMetadataService.get(Matchers.anyString(), Matchers.anyString())).thenReturn(imageMetadata);

        DockerContainerMetadata containerMetadata = new DockerContainerMetadata("000", imageMetadata.getId(), "fogA", instanceId);
        Mockito.when(containerMetadataService.getLatestByInstance(Matchers.anyString())).thenReturn(containerMetadata);
    }

    @Test
    public void initTest() {

        assertThat(environmentInfoService).isNotNull();
        assertThat(redissonClient).isNotNull();

        assertThat(environmentInfoService.getInstanceId()).isNotEmpty();
    }

    @Test
    public void testTravelFifo() {

        AppRequestServiceImpl appRequestService = new AppRequestServiceImpl(redissonClient, containerMetadataService, imageMetadataService, new MetadataManagerConfigProperties(false));

        appRequestService.reset();
        assertThat(appRequestService.getNextRequest(instanceId)).isNull();


        AppRequest requestA = new AppRequest(targetA, instanceId, 1);
        AppRequest requestB = new AppRequest(targetB, instanceId, 1);


        AppRequestResult firstRequestResult = appRequestService.request(1, null, requestA);

        RequestState firstRequestState = appRequestService.requestInfo(firstRequestResult.getInternalId());
        assertThat(firstRequestState).isNotNull();
        assertThat(firstRequestState.isFinished()).isFalse();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        AppRequestResult lastRequestResult = appRequestService.request(1, null, requestB);
        assertThat(firstRequestResult.getInternalId()).isNotEqualToIgnoringCase(lastRequestResult.getInternalId());

        RequestState lastRequestState = appRequestService.requestInfo(lastRequestResult.getInternalId());
        assertThat(lastRequestState).isNotNull();
        assertThat(lastRequestState.isFinished()).isFalse();

        assertThat(appRequestService.getNextRequest(instanceId)).isNotNull();
        assertThat(appRequestService.getNextRequest(instanceId)).isNotNull();
        assertThat(appRequestService.getNextRequest(instanceId)).isNotNull();

        AppRequest firstRequest = appRequestService.getNextRequest(instanceId);
        assertThat(firstRequest).isNotNull();

        AppRequest request = appRequestService.finishMove(instanceId, firstRequest.getTarget());
        assertThat(request).isNotNull();
        assertThat(request.getInstanceId()).isNotEmpty();
        assertThat(appRequestService.requestInfo(firstRequestResult.getInternalId()).isFinished()).isTrue();
        assertThat(appRequestService.requestInfo(lastRequestResult.getInternalId()).isFinished()).isFalse();

        assertThat(appRequestService.finishMove(instanceId, firstRequest.getTarget())).isNull();
        assertThat(appRequestService.requestInfo(firstRequestResult.getInternalId()).isFinished()).isTrue();
        assertThat(appRequestService.requestInfo(lastRequestResult.getInternalId()).isFinished()).isFalse();

        AppRequest lastRequest = appRequestService.getNextRequest(instanceId);
        assertThat(lastRequest).isNotNull();

        //ordering (fifo)
        assertThat(firstRequest.getTarget().isSameFog(requestA.getTarget())).isTrue();
        assertThat(lastRequest.getTarget().isSameFog(requestB.getTarget())).isTrue();

        appRequestService.finishMove(instanceId, lastRequest.getTarget());

        assertThat(appRequestService.getNextRequest(instanceId)).isNull();

        assertThat(appRequestService.finishMove(instanceId, lastRequest.getTarget())).isNull();

        assertThat(appRequestService.requestInfo(firstRequestResult.getInternalId()).isFinished()).isTrue();
        assertThat(appRequestService.requestInfo(lastRequestResult.getInternalId()).isFinished()).isTrue();
    }
}
