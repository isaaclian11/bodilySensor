import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { withFirebase } from '../Firebase';
import { compose } from 'recompose';
import * as AWS from 'aws-sdk';

 
const SignUpPage = () => (
  <div>
    <h1>SignUp</h1>
    <SignUpForm />
  </div>
);
 
const INITIAL_STATE = {
    username: '',
    email: '',
    passwordOne: '',
    passwordTwo: '',
    error: null,
  };

class SignUpFormBase  extends Component {
  constructor(props) {
    super(props);
    this.state = { ...INITIAL_STATE };
  }
 
  onSubmit = event => {
    const { username, email, passwordOne } = this.state;

    this.sendToAWS();   

    this.props.firebase
      .doCreateUserWithEmailAndPassword(email, passwordOne)
      .then(authUser => {
        this.setState({ ...INITIAL_STATE });
        this.props.history.push('/Home');
      })
      .catch(error => {
        this.setState({ error });
      });
     
 
    event.preventDefault();
  };

  sendToAWS = () =>{

    AWS.config.update({
      region: 'us-east-2',
      endpoint: 'dynamodb.us-east-2.amazonaws.com',
      accessKeyId: 'AKIAYPNA3BUHUHWK52FL',
      secretAccessKey: '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'
  });
    var documentClient = new AWS.DynamoDB.DocumentClient();

    var params = {
      TableName: 'users',
      Item: {
        'user_id' : this.state.username, //update to firebase userid
        'email' : this.state.email,
        'first_name' : this.state.firstname,
        'last_name' : this.state.lastname,
        'phone_number' : this.state.phone,
        'user_type' : "PROVIDER",
        
      }
    };
    documentClient.put(params, function(err, data) {
      if (err) {
        console.log("Error", err);
      } else {
        console.log("Success", data);
      }
    });
  }
 
  onChange = event => {
    this.setState({ [event.target.name]: event.target.value });
  };
 
  render() {
    const {
        username,
        email,
        passwordOne,
        passwordTwo,
        error,
        phone,
        firstname,
        lastname,
      } = this.state;

      const isInvalid =
      passwordOne !== passwordTwo ||
      passwordOne === '' ||
      email === '' ||
      username === '' ||
      phone === '' ||
      firstname === '' ||
      lastname === '';

    return (
      <form onSubmit={this.onSubmit}>
         <input
          name="username"
          value={username}
          onChange={this.onChange}
          type="text"
          placeholder="User Name"
        />
        <input
          name="firstname"
          value={firstname}
          onChange={this.onChange}
          type="text"
          placeholder="First Name"
        />
       <input
          name="lastname"
          value={lastname}
          onChange={this.onChange}
          type="text"
          placeholder="Last Name"
        />
         <input
          name="phone"
          value={phone}
          onChange={this.onChange}
          type="text"
          placeholder="Phone 555-555-5555"
        />
        <input
          name="email"
          value={email}
          onChange={this.onChange}
          type="text"
          placeholder="Email Address"
        />
        <input
          name="passwordOne"
          value={passwordOne}
          onChange={this.onChange}
          type="password"
          placeholder="Password"
        />
        <input
          name="passwordTwo"
          value={passwordTwo}
          onChange={this.onChange}
          type="password"
          placeholder="Confirm Password"
        />
        <button disabled={isInvalid} type="submit">Sign Up</button>
 
        {error && <p>{error.message}</p>}
      </form>
    );
  }
}
 
const SignUpLink = () => (
  <p>
    Don't have an account? <li><Link class="showme" to={'/signup'}>Sign Up</Link></li>
  </p>
);

const SignUpForm = compose(
    withRouter,
    withFirebase,
  )(SignUpFormBase);

export default SignUpPage;
 
export { SignUpForm, SignUpLink };