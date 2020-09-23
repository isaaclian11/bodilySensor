import React, { Component } from 'react';
import { Container, Button, Jumbotron } from 'react-bootstrap';
import './Home.css';
import firebaseApp from 'firebase/app';
import 'firebase/auth';
import * as AWS from 'aws-sdk';
import PostData from './post.json'



export default class Home extends Component {
    constructor() {
        super();
        this.state = { data: {'userID': 'zbc123'} };
      }

      

    render() {


        //firebaseApp.auth().setPersistence(firebaseApp.auth.Auth.Persistence.SESSION)
        const user = firebaseApp.auth().currentUser;
        //const awsStuff = callAWS;
        if (user) {
            AWS.config.update({
                region: 'us-east-2',
                endpoint: 'dynamodb.us-east-2.amazonaws.com',
                accessKeyId: 'AKIAYPNA3BUHUHWK52FL',
                secretAccessKey: '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'
            });
      
            this.dynamodb = new AWS.DynamoDB();
            this.docClient = new AWS.DynamoDB.DocumentClient();
            var table = "Provider_Patients";
      
            var name = 1;
            
            // var params = {
            //     TableName: table,
            //     Key:{
            //         "provider_id": name
            //     }
            // };

            var params = {
                TableName : table,
                KeyConditionExpression: "#yr = :yyyy",
                ExpressionAttributeNames:{
                    "#yr": "id"
                },
                ExpressionAttributeValues: {
                    ":yyyy": name
                }
            };

            this.docClient.query(params, function(err, data) {
                if (err) {
                    console.error("Unable to query. Error:", JSON.stringify(err, null, 2));
                } else {
                    console.log("Query succeeded.");
                    data.Items.forEach(function(item) {
                        console.log(" -", item.patient_id + ": " + item.provider_id);
                    });
                }
            });

            //console.log(awsStuff);
                return (<div>
                    {PostData.map((person, index) => (
                        <p key={index}>Hello, {person.name}!</p>
                    ))}
                    </div>);  
        }
        else {
            return (
                <Container >
                    <Jumbotron>
                        <h2>You are not meant to be here!</h2>
                        <p>Here you will be monitored.</p>
                    </Jumbotron>
                    <Button onClick={() => this.props.history.push('/')}>Login</Button>
                </Container >
            )
        }

    }
}