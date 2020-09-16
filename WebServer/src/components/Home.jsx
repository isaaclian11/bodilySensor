import React, {Component} from 'react';
import {  Container , Button, Jumbotron } from 'react-bootstrap';
import './Home.css';



export default class Home  extends Component {

    render() {
        return (
            <Container >
                <Jumbotron>
                    <h2>Welcome to Cloud and Back Personal Tracker!</h2>
                    <p>Here you will be able to monitor you different monitoring devices.</p>
                </Jumbotron>
                <Button  onClick={() => this.props.history.push('/About')}>About</Button>
            </Container >
        )
    }
}