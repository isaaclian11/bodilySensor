import React, { Component } from 'react';
import { Navbar, Nav, NavItem } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import './CustomNavbar.css'
import SignOutButton from './SignOut';

export default class CustomNavbar extends Component {
    render() {
        return (
            <Navbar default collapseOnSelect>
                <Navbar>
                    {/* <Navbar.Brand>
                        <Link to="/home">Cloud n Back</Link>
                    </Navbar.Brand>
                    <Navbar.Toggle /> */}
                </Navbar>
                <Navbar.Collapse>
                    <Nav className="ml-auto">
                        <NavItem>
                            <li className="nav-item"><Link className="nav-link" to="/">SignIn</Link></li>
                        </NavItem>
                        <NavItem>
                            <li>
                                <SignOutButton/>
                            </li>
                        </NavItem>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        )
    }
}
