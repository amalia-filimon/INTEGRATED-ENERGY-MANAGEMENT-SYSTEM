import React from 'react';
import './home.css';
import { Link } from 'react-router-dom';

import backgroundImg from '../commons/images/home_background.jpg';

const backgroundStyle = {
    backgroundPosition: 'center',
    backgroundSize: 'cover',
    backgroundRepeat: 'no-repeat',
    width: "100%",
    height: "100vh", //100% din inaltimea ferestrei
    backgroundImage: `url(${backgroundImg})`
};

function Home() {
    return (
        <div style={backgroundStyle}>
            <div className="t1">Welcome to our Energy Management System!!!</div>
            <Link to="/login">
                <button className="to-login-button">For authentication click here</button>
            </Link>
        </div>
    )
}

export default Home