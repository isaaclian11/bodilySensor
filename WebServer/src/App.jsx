import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import Home from './components/Home';
import About from './components/About';
import SignUpForm from './components/SignUp';

import './App.css';
import Navbar from './components/CustomNavbar';



function App() {
  return (
      <Router>
        <div>
          <Navbar />
          <Route exact path="/" component={SignUpForm} />
          <Route path="/about" component={About} />
          <Route path="/home" component={Home} />
        </div>
      </Router>
  );
}

export default App;
