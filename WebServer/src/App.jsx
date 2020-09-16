import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import Home from './components/Home';
import About from './components/About';
import SignUpForm from './components/SignUp';
import SignInForm from './components/SignIn';


import './App.css';
import Navbar from './components/CustomNavbar';



function App() {
  return (
      <Router>
        <div>
          <Navbar />
          <Route exact path="/" component={SignInForm} />
          <Route path="/about" component={About} />
          <Route path="/home" component={Home} />
          <Route path="/signup" component={SignUpForm} />
        </div>
      </Router>
  );
}

export default App;
