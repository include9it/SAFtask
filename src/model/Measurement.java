package model;

public class Measurement extends AbstractInfo {

    private double measureValue;

    public Measurement(String time, String sensorName, String metricName, String unitSymbol, double measureValue) {
        super(time, sensorName, metricName, unitSymbol);
        this.measureValue = measureValue;
    }
}
