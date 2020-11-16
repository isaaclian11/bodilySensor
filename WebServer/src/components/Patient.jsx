import React, {Component} from 'react';
import {  Container , Button, Jumbotron } from 'react-bootstrap';
import firebaseApp from 'firebase/app';
import 'firebase/auth';
import * as AWS from 'aws-sdk';

export default class About  extends Component {

    constructor() {
        super();
        this.state = { patientID: "8kJE7W3g8xM0wswGT8cozOXPGQy2", heartRate: [], stateHasBeenSet: false, msTime: [] };
        this.pullingPatients = this.pullingPatients.bind(this)
        this.pullingPatients()
    }
    // componentDidMount() {
    //     this.setState({patientID: this.state.patientID.patient});
    //   }

    async pullingPatients() {
        if(this.state.stateHasBeenSet){
        AWS.config.update({
            region: 'us-east-1',
            endpoint: 'dynamodb.us-east-1.amazonaws.com',
            accessKeyId: 'AKIAYPNA3BUHUHWK52FL',
            secretAccessKey: '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'
        });
        var docClient = new AWS.DynamoDB.DocumentClient();
        var user = firebaseApp.auth().currentUser;
        if (user) {
            try {
                var params = {
                    TableName: 'sensor_info',
                    Key: {
                        user_id: "8kJE7W3g8xM0wswGT8cozOXPGQy2"
                    }
                }
                 //await docClient.get(params).promise().then(result => this.setState({ msTime: result.Item.time }))
                 await docClient.get(params).promise().then(result => this.setState({ heartRate: result.Item.heart_rate }))
            //   this.state.patients.map(patient => docClient.get({
            //     TableName: 'users',
            //     Key: {
            //         "user_id": patient
            //     }
            // }).promise().then(rates => this.setState({ heartRate: rates.Item.heart })))
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
    }

    settingPatient(id){
        this.setState({ patientID: id})
        this.pullingPatients()
    }

    render() {
        if(this.state.stateHasBeenSet === false){
            this.settingPatient(this.props.history.location.state.patientID)
            this.pullingPatients()
            this.setState({stateHasBeenSet: true})
        }
        const user = firebaseApp.auth().currentUser;
        console.log(this.state.patientID.patient)
        if(user){
            return (
                <Container >
                         <Jumbotron>
                             <h2>Patient id is: {this.state.patientID.patient} </h2>
                             <p> {this.state.heartRate.map(rate => <p>{rate}</p>)}</p>
                         </Jumbotron>
                         <Button  onClick={() => 
                            this.props.history.push('/Home')}
                            >Main</Button>
                     </Container >
            )
        }
        else{
            return (
                <Container >
                         <Jumbotron>
                             <h2>You are not meant to be here!</h2>
                             <p>Here you will be monitored.</p>
                         </Jumbotron>
                         <Button  onClick={() => this.props.history.push('/')}>Login</Button>
                     </Container >
            )
        }

    }
}