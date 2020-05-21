package model;

public class MeasurementInterval extends AbstractInfo {

    private double measureValueMin;
    private double measureValueMax;

    public MeasurementInterval(String time, String sensorName, String metricName, String unitSymbol, double measureValueMin, double measureValueMax) {
        super(time, sensorName, metricName, unitSymbol);
        this.measureValueMin = measureValueMin;
        this.measureValueMax = measureValueMax;
    }
}
