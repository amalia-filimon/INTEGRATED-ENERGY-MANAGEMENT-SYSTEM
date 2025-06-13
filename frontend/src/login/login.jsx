import React from 'react';
import './login.css';
import {useState} from 'react';
import {jwtDecode as jwt_decode} from "jwt-decode";


import BackgroundImg from '../commons/images/energy1.jpg';
import usernameImg from '../commons/images/username.png';
import passwordImg from '../commons/images/password.png';
import {useEffect} from "react";
import SockJS from "sockjs-client";
import WebSocketContext from '../WebSocketContext';

const backgroundStyle = {
    backgroundPosition: 'center',
    backgroundSize: 'cover',
    backgroundRepeat: 'no-repeat',
    width: "100%",
    height: "100vh", //100% din inaltimea ferestrei
    backgroundImage: `url(${BackgroundImg})`
};

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const [stompClient, setStompClient] = useState(null);

    // useEffect(() => {
    //     const socket = new SockJS('http://localhost:8082/websocket');
    //     const client = window.Stomp.over(socket);
    //
    //     client.connect({}, () => {
    //         setStompClient(client);
    //         console.log('Conectat la WebSocket');
    //     });
    //
    //     return () => {
    //         if (client && client.connected) {
    //             client.disconnect();
    //             console.log('Deconectat de la WebSocket');
    //         }
    //     };
    // }, []);

    const handleSubmit = async () => {
        const data = {
            username: username,
            password: password,
        };

        try {
            const response = await fetch('http://localhost:8080/api/v1/auth/authenticate', {
                //mode: 'no-cors',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                const responseData = await response.json();
                const token = responseData.token;
                const decodedToken = jwt_decode(token);
                localStorage.setItem('userRole', decodedToken.userRole); //cand utilizatorul se autentifica cu succes salvez rolul acestuia in localStorage
                localStorage.setItem('authToken', token); //salvez tokenul in local storage pentru a-l utiliza mai tarziu in requesturi

                const userRole = decodedToken.userRole;
                if (userRole === "USER") {
                    window.location.href = `/client?userId=${decodedToken.userId}`;
                } else {
                    window.location.href = `/admin?userId=${decodedToken.userId}`;
                }
            } else {
                // Autentificarea a eșuat
                setErrorMessage("Username or password incorrect! Try again!");
                console.log("Authentication failed!!!");
            }
        } catch (error) {
            console.error('Eroare la solicitarea de autentificare: ', error);
        }
    };

    return (
    //    <WebSocketContext.Provider value={stompClient}>
            <div style={backgroundStyle}>
                <div className="login-form">
                    <form onSubmit={handleSubmit}>
                        <h2 className="custom-title">Login</h2>
                        <div className="fail"
                             style={{display: errorMessage ? 'block' : 'none', color: 'red'}}> {errorMessage}
                        </div>
                        <div className="input-container">
                            <img
                                src={usernameImg} alt=""
                                className="custom-img" //clasa pt stilizarea imaginii
                            />
                            <input
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                placeholder="Username"
                                className="custom-input" //am adaugat o clasa pt stilizare
                            />
                        </div>
                        <div className="input-container">
                            <img
                                src={passwordImg} alt=""
                                className="custom-img"
                            />
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="Password"
                                className="custom-input" //am adaugat o clasa pt stilizare
                            />
                        </div>
                        <div className="button-container">
                            <button type="button" className="custom-button" onClick={handleSubmit}>Login</button>
                        </div>
                    </form>
                </div>
            </div>
    //    </WebSocketContext.Provider>
    )
}

export default Login