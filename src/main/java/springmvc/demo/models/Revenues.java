package springmvc.demo.models;

import java.util.List;

public class Revenues {

    private List<Revenue> revenues;

    public Revenues(List<Revenue> revenues) {
        this.revenues = revenues;
    }

    public List<Revenue> getRevenues() {
        return revenues;
    }

    public void setRevenues(List<Revenue> revenues) {
        this.revenues = revenues;
    }
}
