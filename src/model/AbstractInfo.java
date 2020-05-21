package model;

public abstract class AbstractInfo {

    private String time;
    private String sensorName;
    private String metricName;
    private String unitSymbol;

    public AbstractInfo(String time, String sensorName, String metricName, String unitSymbol) {
        this.time = time;
        this.sensorName = sensorName;
        this.metricName = metricName;
        this.unitSymbol = unitSymbol;
    }
}
