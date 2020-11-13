import React, {Component} from 'react';
import {  Container , Button, Jumbotron } from 'react-bootstrap';
import firebaseApp from 'firebase/app';

export default class About  extends Component {
    render() {
        const user = firebaseApp.auth().currentUser;
        if(user){
            return (
                <Container >
                         <Jumbotron>
                             <h2>Welcome to Cloud and Back Personal Tracker!</h2>
                             <p>Here you will be able to monitor you different monitoring devices.</p>
                         </Jumbotron>
                         <Button  onClick={() => this.props.history.push('/Home')}>About</Button>
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