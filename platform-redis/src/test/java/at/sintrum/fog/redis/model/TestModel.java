package at.sintrum.fog.redis.model;

import org.joda.time.DateTime;

/**
 * Created by Michael Mittermayr on 26.07.2017.
 */
public class TestModel {
    private DateTime someTime;

    public TestModel() {

    }

    public TestModel(DateTime someTime) {
        this.someTime = someTime;
    }

    public DateTime getSomeTime() {
        return someTime;
    }

    public void setSomeTime(DateTime someTime) {
        this.someTime = someTime;
    }
}