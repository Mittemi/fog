package at.sintrum.fog.application.service;

import at.sintrum.fog.application.core.api.dto.RequestAppDto;
import at.sintrum.fog.application.core.service.TravelingCoordinationService;
import at.sintrum.fog.application.core.service.TravelingCoordinationServiceImpl;
import at.sintrum.fog.core.dto.FogIdentification;
import at.sintrum.fog.core.service.EnvironmentInfoService;
import at.sintrum.fog.metadatamanager.api.dto.AppState;
import at.sintrum.fog.metadatamanager.api.dto.ApplicationStateMetadata;
import at.sintrum.fog.metadatamanager.client.api.ApplicationStateMetadataClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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
    private ApplicationStateMetadataClient applicationStateMetadataClient;

    private FogIdentification targetA = new FogIdentification("a", 0);
    private FogIdentification targetB = new FogIdentification("b", 0);

    private final String unitTestInstanceId = "UnitTestInstanceId";


    @Before
    public void initMocks() {
        Mockito.when(environmentInfoService.getInstanceId()).thenReturn(unitTestInstanceId);
        Mockito.when(environmentInfoService.getApplicationName()).thenReturn("UnitTestApplication");
    }

    @Test
    public void initTest() {

        assertThat(environmentInfoService).isNotNull();
        assertThat(redissonClient).isNotNull();
        assertThat(applicationStateMetadataClient).isNotNull();

        assertThat(environmentInfoService.getInstanceId()).isNotEmpty();
    }


    @Test
    public void testWithoutSpamming() {

        Mockito.when(applicationStateMetadataClient.getById(Mockito.anyString())).thenReturn(new ApplicationStateMetadata(unitTestInstanceId, 0, targetA, AppState.Running));

        TravelingCoordinationService travelingCoordinationService = new TravelingCoordinationServiceImpl(redissonClient, environmentInfoService, applicationStateMetadataClient);
        travelingCoordinationService.reset();

        assertThat(travelingCoordinationService.getNextTarget()).isNull();
        List<FogIdentification> targets = travelingCoordinationService.getTargets();
        assertThat(targets).isNotNull();
        assertThat(targets).isEmpty();

        assertThat(travelingCoordinationService.requestMove(new RequestAppDto(targetA))).isTrue();
        assertThat(travelingCoordinationService.requestMove(new RequestAppDto(targetA))).isFalse();

        assertThat(travelingCoordinationService.getNextTarget().isSameFog(targetA)).isTrue();
        assertThat(travelingCoordinationService.getNextTarget().isSameFog(targetA)).isTrue();

        assertThat(travelingCoordinationService.finishMove(targetA)).isTrue();

        assertThat(travelingCoordinationService.getNextTarget()).isNull();
    }

    @Test
    @Ignore
    public void testAllowQueueSpamming() {
        Mockito.when(applicationStateMetadataClient.getById(Mockito.anyString())).thenReturn(new ApplicationStateMetadata(unitTestInstanceId, 0, targetA, AppState.Running));

        TravelingCoordinationService travelingCoordinationService = new TravelingCoordinationServiceImpl(redissonClient, environmentInfoService, applicationStateMetadataClient);
        travelingCoordinationService.reset();

        assertThat(travelingCoordinationService.getNextTarget()).isNull();
        List<FogIdentification> targets = travelingCoordinationService.getTargets();
        assertThat(targets).isNotNull();
        assertThat(targets).isEmpty();

        assertThat(travelingCoordinationService.requestMove(new RequestAppDto(targetA))).isTrue();
        assertThat(travelingCoordinationService.requestMove(new RequestAppDto(targetA))).isTrue();

        assertThat(travelingCoordinationService.getNextTarget().isSameFog(targetA)).isTrue();
        assertThat(travelingCoordinationService.getNextTarget().isSameFog(targetA)).isTrue();

        assertThat(travelingCoordinationService.finishMove(targetA)).isTrue();

        assertThat(travelingCoordinationService.getNextTarget()).isNull();
    }

}
