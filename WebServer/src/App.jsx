import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import Home from './components/Home';
import About from './components/About';
import './App.css';
import Navbar from './components/CustomNavbar';


function App() {
  return (
      <Router>
        <div>
          <Navbar />
          <Route exact path="/" component={Home} />
          <Route path="/about" component={About} />
        </div>
      </Router>
  );
}

export default App;
