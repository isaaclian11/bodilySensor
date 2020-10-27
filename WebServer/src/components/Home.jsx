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
        this.state = { Email: 'Need To Change' };
        this.pullingEmail = this.pullingEmail.bind(this)
        this.pullingEmail()
      }


      async pullingEmail() {

        AWS.config.update({
                    region: 'us-east-2',
                    endpoint: 'dynamodb.us-east-2.amazonaws.com',
                    accessKeyId: 'AKIAYPNA3BUHUHWK52FL',
                    secretAccessKey: '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'
                });

        var docClient = new AWS.DynamoDB.DocumentClient();


          try{
            var params = {
                TableName: 'users',
                Key:{
                    "user_id": 'EbGp7qwQWlfaRz2PD0X0iSL0ivl2'
                }
            }; 
            var result = await docClient.get(params).promise().then(result => this.setState({
                Email: result.Item.email
              }))
          } catch (error) {
              console.error(error);
          }
      }
                // async getData() {
                //     AWS.config.update({
                //         region: 'us-east-2',
                //         endpoint: 'dynamodb.us-east-2.amazonaws.com',
                //         accessKeyId: 'AKIAYPNA3BUHUHWK52FL',
                //         secretAccessKey: '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'
                //     });
                
                //     //this.dynamodb = new AWS.DynamoDB();
                //     this.docClient = new AWS.DynamoDB.DocumentClient();
                
                //     var params = {
                //         TableName: 'users',
                //         Key:{
                //             "user_id": 'EbGp7qwQWlfaRz2PD0X0iSL0ivl2'
                //         }
                //     };
                        
                //     var documentClient = new AWS.DynamoDB.DocumentClient();
                //     var dataPulled;
                //     this.setState({ Email: await documentClient.get(params, function(err, data) {
                //         if (err){ 
                            
                //             dataPulled = "YOU F'ed Up";
                //         }
                //         else{
                //             console.log(data.Item.email+" in Function");
                //             // this.setState({ Email: data.Item.email });
                //             dataPulled = "IT WORKED!";
                //         }
                //         }).Item.email})
                // }



            //   updateState(dataPassed) {
            //     (async () => {
            //        const data = await this.pull_data();
            //        console.log(data);
            //        return JSON.stringify(data);
            //     })();
            // }
                
            //   async pull_data(){
            //     AWS.config.update({
            //         region: 'us-east-2',
            //         endpoint: 'dynamodb.us-east-2.amazonaws.com',
            //         accessKeyId: 'AKIAYPNA3BUHUHWK52FL',
            //         secretAccessKey: '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'
            //     });

            //     //this.dynamodb = new AWS.DynamoDB();
            //     this.docClient = new AWS.DynamoDB.DocumentClient();

            //     var params = {
            //         TableName: 'users',
            //         Key:{
            //             "user_id": 'EbGp7qwQWlfaRz2PD0X0iSL0ivl2'
            //         }
            //     };
                    
            //     var documentClient = new AWS.DynamoDB.DocumentClient();
            //     var dataPulled;
            //     var getting = documentClient.get(params, function(err, data) {
            //         if (err){ 
            //             dataPulled = "YOU F'ed Up";
            //         }
            //         else{
            //             console.log(data.Item.email+" in Function");
            //             //this.updateState(data);
            //             dataPulled = "IT WORKED!";
            //         }
            //       }).promise;
            //       return dataPulled;
            //   }

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
                 I just need this to work {this.state.Email} sigh..
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