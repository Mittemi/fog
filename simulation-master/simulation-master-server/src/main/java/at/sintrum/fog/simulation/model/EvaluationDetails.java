package at.sintrum.fog.simulation.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 15.11.2017.
 */
public class EvaluationDetails {

    private String id;

    private List<RequestEvalDetails> requestDetails;

    public EvaluationDetails() {
        requestDetails = new LinkedList<>();
    }

    public EvaluationDetails(String id) {
        this();
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RequestEvalDetails> getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(List<RequestEvalDetails> requestDetails) {
        this.requestDetails = requestDetails;
    }
}
