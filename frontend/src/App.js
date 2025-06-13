import './App.css';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Login from './login/login';
import Home from './home/home';
import Administrator from './administrator/administrator';
import Client from './client/client';
import RoleChecker from "./RoleChecker";
import {useEffect, useState} from "react";
import SockJS from "sockjs-client";
import WebSocketContext from './WebSocketContext';

function App() {
    const [stompClient, setStompClient] = useState(null);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8083/websocket-chat');
        const client = window.Stomp.over(socket);

        client.connect({}, () => {
            setStompClient(client);
            console.log('Conectat la WebSocket in server 8083');
        });

        return () => {
            if (client && client.connected) {
                client.disconnect();
                console.log('Deconectat de la WebSocket in server 8083');
            }
        };
    }, []);

    return (
        <WebSocketContext.Provider value={stompClient}>
            <Router>
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/client" element={
                        <RoleChecker roles={['USER']}>
                            <Client/>
                        </RoleChecker>
                    }/>
                    <Route path="/admin" element={
                        <RoleChecker roles={['ADMIN']}>
                            <Administrator/>
                        </RoleChecker>
                    }/>
                </Routes>
            </Router>
        </WebSocketContext.Provider>
    );
}

export default App;
