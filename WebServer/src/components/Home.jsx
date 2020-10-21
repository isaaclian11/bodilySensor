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

      
      async pull_data(){
        AWS.config.update({
            region: 'us-east-2',
            endpoint: 'dynamodb.us-east-2.amazonaws.com',
            accessKeyId: 'AKIAYPNA3BUHUHWK52FL',
            secretAccessKey: '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'
        });
  
        this.dynamodb = new AWS.DynamoDB();
        this.docClient = new AWS.DynamoDB.DocumentClient();
  
        var params = {
            TableName: 'users',
            Key:{
                "user_id": 'EbGp7qwQWlfaRz2PD0X0iSL0ivl2'
            }
        };
          
        var documentClient = new AWS.DynamoDB.DocumentClient();
          
        // var toReturn2 = "BOOOO";

        //  var toReturn =  
        //  {
        //     "name":"John",
        //     "age":30,
        //     "cars":[ "Ford", "BMW", "Fiat" ]
        //   };

         return await documentClient.get(params, function(err, data) {
            if (err){ 
                return "YOU F'ed Up"
            }
            else{
                console.log(data.Item.email);
                return "BOOOO";
            }
          }).promise;


          //return "I HATE THIS:" + toReturn2;
      }

    render() {


        //firebaseApp.auth().setPersistence(firebaseApp.auth.Auth.Persistence.SESSION)
        const user = firebaseApp.auth().currentUser;
        //const awsStuff = callAWS;
        if (user) {
           
              return (
                <div>
                 I just need this to work {this.pull_data()} sigh..
                  </div>);
              
            
            // var params = {
            //     TableName: 'users',
            //     Item: {
            //       'user_id' : "testing",
            //       'email' : "testing",
            //       'first_name' : "testing",
            //       'last_name' : "testing",
            //       'phone_number' : "testing",
            //       'user_type' : "Provider",
                  
            //     }
            //   };
            //   documentClient.put(params, function(err, data) {
            //     if (err) {
            //       console.log("Error", err);
            //     } else {
            //       console.log("Success", data);

            //     }
            //   });


            //console.log(awsStuff);
                // return (<div>
                //     {PostData.map((person, index) => (
                //         <p key={index}>Hello, {person.name}!</p>
                //     ))}
                //     </div>);  
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