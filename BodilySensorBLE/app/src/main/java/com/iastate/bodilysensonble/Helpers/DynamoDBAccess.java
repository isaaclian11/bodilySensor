package com.iastate.bodilysensonble.Helpers;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.PutItemOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.DynamoDBList;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;


public class DynamoDBAccess {
    private final String IDENTITY_POOL_ID = "us-east-2:bf60357a-3165-4185-89f6-ece495de39d0";
    private final String USER_TABLE = "users";

    private CognitoCachingCredentialsProvider credentialProvider;
    private AmazonDynamoDBClient dynamoDBClient;
    private Table userTable;

    public static final String USER_ID = "user_id";
    public static final String HEART_RATE_INFO = "heart_rate_info";
    public static final String HEART_RATE_FREQUENCY = "heart_rate_frequency";
    public static final String SESSION_ID = "session_id";
    public static final String RESPIRATION_RATE_INFO = "respiration_rate_info";
    public static final String RESPIRATION_RATE_FREQUENCY = "respiration_rate_frequency";
    public static final String WEIGHT_INFO = "weight_info";
    public static final String WEIGHT_MEASUREMENT = "weight_measurement";
    public static final String TIME_MS = "time_ms";


    private static volatile DynamoDBAccess instance;

    private DynamoDBAccess(Context context){
        credentialProvider = new CognitoCachingCredentialsProvider(context, IDENTITY_POOL_ID, Regions.US_EAST_2);
        dynamoDBClient = new AmazonDynamoDBClient(credentialProvider);
        dynamoDBClient.setRegion(Region.getRegion(Regions.US_EAST_2));
        userTable = Table.loadTable(dynamoDBClient, USER_TABLE);
    }

    public static synchronized DynamoDBAccess getInstance(Context context){
        if(instance==null)
            return new DynamoDBAccess(context);
        return instance;
    }

    public Boolean putItem(Document document){
        if(!document.containsKey("user_id"))
            return false;
        userTable.putItem(document);
        return true;
    }

    public Boolean addNewMeasurement(MeasurementModel model){
        String user_id = model.getUser_id();
        Document newMeasurement = new Document();
        newMeasurement.put(TIME_MS, model.getTime_ms());
        newMeasurement.put(SESSION_ID, model.getSession_id());

        Document currentDoc = userTable.getItem(new Primitive(user_id));
        Document result;
        if(model.getDataType()== MeasurementModel.SENSORS.HEART){
            newMeasurement.put(HEART_RATE_FREQUENCY, model.getRaw_measurement());
            if(currentDoc.get(HEART_RATE_INFO)!=null){
                DynamoDBList heart_rate_info = currentDoc.get(HEART_RATE_INFO).asDynamoDBList();
                heart_rate_info.add(newMeasurement);
                currentDoc.put(HEART_RATE_INFO, heart_rate_info);
                result = userTable.putItem(currentDoc,
                        new PutItemOperationConfig().withReturnValues(ReturnValue.UPDATED_NEW));
            }else{
                DynamoDBList heart_rate_info = new DynamoDBList();
                heart_rate_info.add(newMeasurement);
                currentDoc.put(HEART_RATE_INFO, heart_rate_info);
                result = userTable.putItem(currentDoc,
                        new PutItemOperationConfig().withReturnValues(ReturnValue.UPDATED_NEW));
            }
        }
        else if(model.getDataType()== MeasurementModel.SENSORS.RESPIRATION){
            newMeasurement.put(RESPIRATION_RATE_FREQUENCY, model.getRaw_measurement());
            if(currentDoc.get(RESPIRATION_RATE_INFO)!=null){
                DynamoDBList respiration_rate_info = currentDoc.get(RESPIRATION_RATE_INFO).asDynamoDBList();
                respiration_rate_info.add(newMeasurement);
                currentDoc.put(RESPIRATION_RATE_INFO, respiration_rate_info);
                result = userTable.putItem(currentDoc,
                        new PutItemOperationConfig().withReturnValues(ReturnValue.UPDATED_NEW));
            }else{
                DynamoDBList respiration_rate_info = new DynamoDBList();
                respiration_rate_info.add(newMeasurement);
                currentDoc.put(RESPIRATION_RATE_INFO, respiration_rate_info);
                result = userTable.putItem(currentDoc,
                        new PutItemOperationConfig().withReturnValues(ReturnValue.UPDATED_NEW));
            }
        }else{
            newMeasurement.put(WEIGHT_MEASUREMENT, model.getRaw_measurement());
            if(currentDoc.get(WEIGHT_INFO)!=null){
                DynamoDBList weight_info = currentDoc.get(WEIGHT_INFO).asDynamoDBList();
                weight_info.add(newMeasurement);
                currentDoc.put(WEIGHT_INFO, weight_info);
                result = userTable.putItem(currentDoc,
                        new PutItemOperationConfig().withReturnValues(ReturnValue.UPDATED_NEW));
            }else{
                DynamoDBList respiration_rate_info = new DynamoDBList();
                respiration_rate_info.add(newMeasurement);
                currentDoc.put(WEIGHT_INFO, respiration_rate_info);
                result = userTable.putItem(currentDoc,
                        new PutItemOperationConfig().withReturnValues(ReturnValue.UPDATED_NEW));
            }
        }

        return result != null;
    }

}
