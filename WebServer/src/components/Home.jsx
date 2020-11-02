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
        this.state = { Email: 'Need To Change', patients: [], heartRate: [] };
        this.pullingPatients = this.pullingPatients.bind(this)
        this.pullingPatients()
    }


    async pullingPatients() {
        AWS.config.update({
            region: 'us-east-2',
            endpoint: 'dynamodb.us-east-2.amazonaws.com',
            accessKeyId: 'AKIAYPNA3BUHUHWK52FL',
            secretAccessKey: '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'
        });
        var docClient = new AWS.DynamoDB.DocumentClient();
        var user = firebaseApp.auth().currentUser;
        if (user) {
            try {
                var params = {
                    TableName: 'users',
                    Key: {
                        "user_id": user.uid
                    }
                }
                var fistName = await docClient.get(params).promise().then(result => this.setState({ Email: result.Item.first_name }))
                var listPatients = await docClient.get(params).promise().then(result => this.setState({ patients: result.Item.test }))
              this.state.patients.map(patient => docClient.get({
                TableName: 'users',
                Key: {
                    "user_id": patient
                }
            }).promise().then(rates => this.setState({ heartRate: rates.Item.heart })))
            //     var whatATest = await docClient.get({
            //     TableName: 'users',
            //     Key: {
            //         "user_id": "testing"
            //     }
            // }).promise().then(results => this.setState({ heartRate: results.Item.heart }))
            } catch (error) {
                console.error(error);
            }
        }
    }

     getHeartRate() {
        // AWS.config.update({
        //     region: 'us-east-2',
        //     endpoint: 'dynamodb.us-east-2.amazonaws.com',
        //     accessKeyId: 'AKIAYPNA3BUHUHWK52FL',
        //     secretAccessKey: '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'
        // });
        // var docClient = new AWS.DynamoDB.DocumentClient();
        //     try {
        //         var params = {
        //             TableName: 'users',
        //             Key: {
        //                 "user_id": user_UID
        //             }
        //         };
        //         var firstName = await docClient.get(params).promise().then(result => result.Item.first_name)
        //         return firstName
        //         //var heartRates = await docClient.get(params).promise().then(result => result.Item.heart_rate_info)
        //     } catch (error) {
        //         console.error(error);
        //     }
        this.setState({heartRate: [11,10]});
    }

    render() {


        //firebaseApp.auth().setPersistence(firebaseApp.auth.Auth.Persistence.SESSION)
        const user = firebaseApp.auth().currentUser;
        // var toPrint = this.pullingEmail();
        console.log(this.state.Email)
        //const dataReturned = this.state.Email;
        //console.log(toPrint);


        if (user) {

            return (
                <div>
                    <p>
                        Welcome {this.state.Email}!</p>
                    <p> Here is a list of your patients: {this.state.patients.map(patient => 
                       // <p><Button onClick={this.getHeartRate()}>{patient
                            <p><Button >{patient
                        // this.getHeartRate(patient).map(rate => 
                        //     <p>{rate}</p>)
                        }</Button></p>)
                    }</p>
                    {this.state.heartRate.map(rate => <p>{rate}</p>)}
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