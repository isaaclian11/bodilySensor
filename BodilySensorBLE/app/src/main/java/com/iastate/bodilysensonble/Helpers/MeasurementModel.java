package com.iastate.bodilysensonble.Helpers;

public class MeasurementModel {
    private String user_id, session_id;
    private double raw_measurement;
    private double time_ms;
    private SENSORS dataType;

    public MeasurementModel(String user_id){
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }


    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public double getRaw_measurement() {
        return raw_measurement;
    }

    public void setRaw_measurement(double raw_measurement) {
        this.raw_measurement = raw_measurement;
    }

    public double getTime_ms() {
        return time_ms;
    }

    public void setTime_ms(long time_ms) {
        this.time_ms = time_ms;
    }

    public SENSORS getDataType() {
        return dataType;
    }

    public void setDataType(SENSORS dataType) {
        this.dataType = dataType;
    }

    public enum SENSORS{
        HEART, RESPIRATION, WEIGHT
    }
}
