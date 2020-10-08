package com.iastate.bodilysensonble.Helpers;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class DynamoDBAccess {
    private final String IDENTITY_POOL_ID = "us-east-2:bf60357a-3165-4185-89f6-ece495de39d0";
    private final String USER_TABLE = "users";

    private CognitoCachingCredentialsProvider credentialProvider;
    private AmazonDynamoDBClient dynamoDBClient;
    private Table userTable;

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
}
