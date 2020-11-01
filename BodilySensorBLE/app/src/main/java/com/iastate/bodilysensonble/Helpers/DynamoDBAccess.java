package com.iastate.bodilysensonble.Helpers;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.PutItemOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;


public class DynamoDBAccess {
    private static final String IDENTITY_POOL_ID = "us-east-1:406ce4dd-3f73-4907-bf48-2950fb8a1fa9";
    private static final String SENSOR_DATA_TABLE = "sensor_data";
    private static final String USER_TABLE = "users";
    private static final int USER_INSTANCE = 0;
    private static final int SENSOR_INSTANCE = 1;

    private CognitoCachingCredentialsProvider credentialProvider;
    private AmazonDynamoDBClient dynamoDBClient;
    private Table sensorDataTable;

    public static final String USER_ID = "user_id";
    public static final String SENSOR_TYPE = "sensor_type";
    public static final String PULSE_SENSOR = "PULSE_SENSOR";
    public static final String WEIGHT_SENSOR = "WEIGHT_SENSOR";
    public static final String RESPIRATION_SENSOR = "RESPIRATION_SENSOR";
    public static final String VALUE = "value";
    public static final String SESSION_ID = "session_id";
    public static final String TIME_MS = "time_ms";


    private static volatile DynamoDBAccess sensorInstance;
    private static volatile DynamoDBAccess userInstance;

    private DynamoDBAccess(Context context, int type){
        credentialProvider = new CognitoCachingCredentialsProvider(context, IDENTITY_POOL_ID, Regions.US_EAST_1);
        dynamoDBClient = new AmazonDynamoDBClient(credentialProvider);
        dynamoDBClient.setRegion(Region.getRegion(Regions.US_EAST_1));
        if(type==USER_INSTANCE)
            sensorDataTable = Table.loadTable(dynamoDBClient, USER_TABLE);
        else if(type==SENSOR_INSTANCE)
            sensorDataTable = Table.loadTable(dynamoDBClient, SENSOR_DATA_TABLE);
    }

    public static synchronized DynamoDBAccess getUserInstance(Context context){
        if(userInstance==null)
            return new DynamoDBAccess(context, USER_INSTANCE);
        return userInstance;
    }

    public static synchronized DynamoDBAccess getSensorInstance(Context context){
        if(userInstance==null)
            return new DynamoDBAccess(context, SENSOR_INSTANCE);
        return userInstance;
    }

    public Boolean putItem(Document document){
        if(!document.containsKey(USER_ID))
            return false;
        Document result = sensorDataTable.putItem(document,
                new PutItemOperationConfig().withReturnValues(ReturnValue.UPDATED_NEW));
        return result!=null;
    }

    public Boolean addNewMeasurement(MeasurementModel model){
        String user_id = model.getUser_id();
        Document newMeasurement = new Document();
        newMeasurement.put(USER_ID, user_id);
        newMeasurement.put(TIME_MS, model.getTime_ms());
        newMeasurement.put(SESSION_ID, model.getSession_id());

        Document result;
        if(model.getDataType()== MeasurementModel.SENSORS.PULSE){
            newMeasurement.put(VALUE, model.getRaw_measurement());
            newMeasurement.put(SENSOR_TYPE, PULSE_SENSOR);
            result = sensorDataTable.putItem(newMeasurement,
                    new PutItemOperationConfig().withReturnValues(ReturnValue.UPDATED_NEW));
        }
        else if(model.getDataType()== MeasurementModel.SENSORS.RESPIRATION){
            newMeasurement.put(VALUE, model.getRaw_measurement());
            newMeasurement.put(SENSOR_TYPE, RESPIRATION_SENSOR);
            result = sensorDataTable.putItem(newMeasurement,
                    new PutItemOperationConfig().withReturnValues(ReturnValue.UPDATED_NEW));
        }else{
            newMeasurement.put(VALUE, model.getRaw_measurement());
            newMeasurement.put(SENSOR_TYPE, WEIGHT_SENSOR);
            result = sensorDataTable.putItem(newMeasurement,
                    new PutItemOperationConfig().withReturnValues(ReturnValue.UPDATED_NEW));
        }

        return result != null;
    }

}
