package org.openalpr;

import org.openalpr.model.Result;

import java.io.Serializable;
import java.util.List;

import sapphire.app.SapphireObject;
import sapphire.policy.MigrationPolicy;

//public class Results implements SapphireObject<MigrationPolicy> {
public class Results implements Serializable {

    private final Double epoch_time;

    private final Double processing_time_ms;

    private final List<Result> results;

//    private List<Result> newResults;

    public Results(Double epoch_time, Double processing_time_ms, List<Result> results) {
        this.epoch_time = epoch_time;
        this.processing_time_ms = processing_time_ms;
        this.results = results;
    }

    public Double getEpochTime() {
        return epoch_time;
    }

    public Double getProcessingTimeMs() {
        return processing_time_ms;
    }

    public List<Result> getResults() {
        return results;
    }
//
//    public void setNewResults(List<Result> results) {
//        this.newResults = results;
//    }
}
