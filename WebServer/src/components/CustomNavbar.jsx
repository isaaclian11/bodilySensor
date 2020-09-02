import React, { Component } from 'react';
import { Navbar, Nav, NavItem } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import './CustomNavbar.css'

export default class CustomNavbar extends Component {
    render() {
        return (
            <Navbar default collapseOnSelect>
                <Navbar>
                    <Navbar.Brand>
                        <Link to="/">Cloud n Back</Link>
                    </Navbar.Brand>
                    <Navbar.Toggle />
                </Navbar>
                <Navbar.Collapse>
                    <Nav className="ml-auto">
                        <NavItem>
                            <Nav.Link eventKey={1} componentClass={Link} to="/">
                            Home
                            </Nav.Link>
                        </NavItem>    
                        <NavItem>
                            <Nav.Link eventKey={2} componentClass={Link} to="/">
                            About
                            </Nav.Link>
                        </NavItem>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        )
    }
}
