package at.sintrum.fog.simulation.api;

import at.sintrum.fog.metadatamanager.api.dto.AppRequestInfo;
import at.sintrum.fog.simulation.model.EvaluationDetails;
import at.sintrum.fog.simulation.model.EvaluationQuickInfo;
import at.sintrum.fog.simulation.model.RequestEvalDetails;
import at.sintrum.fog.simulation.simulation.mongo.FullSimulationResult;
import at.sintrum.fog.simulation.simulation.mongo.respositories.FullSimulationResultRepository;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.base.AbstractInstant;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Michael Mittermayr on 31.10.2017.
 */
@RestController
@RequestMapping("evaluation")
public class EvaluationController {

    private final FullSimulationResultRepository fullSimulationResultRepository;

    public EvaluationController(FullSimulationResultRepository fullSimulationResultRepository) {
        this.fullSimulationResultRepository = fullSimulationResultRepository;
    }

    @RequestMapping(value = "runs", method = RequestMethod.GET)
    public List<EvaluationQuickInfo> getRuns() {
        return StreamSupport
                .stream(fullSimulationResultRepository.findAll().spliterator(), false)
                .map(fullSimulationResult -> new EvaluationQuickInfo(fullSimulationResult.getId(), buildName(fullSimulationResult)))
                .collect(Collectors.toList());
    }

    private String buildName(FullSimulationResult fullSimulationResult) {
        return fullSimulationResult.getExecutionInfo().getName()
                + ": "
                + (fullSimulationResult.getExecutionResult().isUseAuctioning() ? "(Auction) " : "(Classic) ")
                + fullSimulationResult.getExecutionResult().getStart();
    }

    @RequestMapping(value = "details/{id}", method = RequestMethod.GET)
    public EvaluationDetails getEvaluation(@PathVariable("id") String id) {
        FullSimulationResult one = fullSimulationResultRepository.findOne(id);

        if (one == null) return null;

        EvaluationDetails evaluationDetails = new EvaluationDetails(id);

        DateTime minDate = one.getFinishedRequests().stream().map(x -> x.getCreationDate()).min(AbstractInstant::compareTo).orElse(null);

        evaluationDetails.setRequestDetails(one.getFinishedRequests().stream().map(x -> toRequestEvalDetails(x, minDate)).collect(Collectors.toList()));

        return evaluationDetails;
    }

    @RequestMapping(value = "full/{id}", method = RequestMethod.GET)
    public FullSimulationResult getFullResult(@PathVariable("id") String id) {
        return fullSimulationResultRepository.findOne(id);
    }

    private RequestEvalDetails toRequestEvalDetails(AppRequestInfo x, DateTime minDate) {

        RequestEvalDetails requestEvalDetails = new RequestEvalDetails(
                x.getInternalId(),
                x.getTargetFog(),
                x.getCredits(),
                Seconds.secondsBetween(x.getCreationDate(), x.getFinishedDate()).getSeconds(),
                Seconds.secondsBetween(minDate, x.getCreationDate()).getSeconds(),
                x.getAppRequest().getInstanceId());

        return requestEvalDetails;
    }
}
