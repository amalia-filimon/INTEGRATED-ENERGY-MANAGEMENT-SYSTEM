import React, {useState, useEffect, useContext} from 'react';
import {useLocation} from 'react-router-dom';
import Table from "../client/components/Table";
import './client.css';
import WebSocketContext from '../WebSocketContext';
import 'react-datepicker/dist/react-datepicker.css';
import EnergyConsumptionChart from './components/EnergyConsumptionChart';
import ChatBox from "./components/ChatBox";


const backgroundStyleClient = {
    backgroundPosition: 'center',
    backgroundSize: 'cover',
    backgroundRepeat: 'no-repeat',
    width: "100%",
    height: "100vh", //100% din inaltimea ferestrei

};

function Client() {
    //preiau userId din URL
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const userId = queryParams.get('userId');

    const [devices, setDevices] = useState([]);
    const stompClient = useContext(WebSocketContext);
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [graphicData, setGraphicData] = useState([]);

    const authToken = localStorage.getItem('authToken'); //preiau tokenul de autentificare din local storage

    const handleDateChange = (event) => {
        //setSelectedDate(event.target.value);
        const selectedDate = new Date(event.target.value);

        if (!isNaN(selectedDate.getTime())) {
            setSelectedDate(selectedDate);
        }
    };

    // const selectCalendar = () => {
    //     console.log('Fetching data for:', selectedDate);

    // };

    async function sendDateFromCalendar() {
        const formattedDate = selectedDate.toISOString().split('T')[0];
        const requestData = {
            data: formattedDate,
            devices: devices
        };
        try {
            const response = await fetch('http://localhost:8082/api/v1/auth/calendar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`,
                },
                //body: JSON.stringify(selectedDate),
                //body:formattedDate,
                body: JSON.stringify(requestData),
            });

            if (response.ok) {
                const responseData = await response.json();
                console.log("Datele preluate: " + JSON.stringify(responseData, null, 2));
                setGraphicData(responseData);
            } else {
                console.log("The date from the calendar couldn't be processed!!!");
            }
        } catch (error) {
            console.error('Eroare la trimitere: ', error);
        }
    }

    useEffect(() => {
        async function fetchDevices() {
            try {
                const response = await fetch(`http://localhost:8081/api/v1/auth/user/${userId}`, {
                    headers:{
                        'Authorization': `Bearer ${authToken}`,
                    },
                });

                if (!response.ok) {
                    throw new Error('Eroare la preluarea datelor de pe server');
                }

                const dataDevices = await response.json();
                //console.log("Primul device: ", dataDevices[0]);
                setDevices(dataDevices);
            } catch (error) {
                console.error('A aparut o eroare la preluarea device-urilor:', error);
            }
        }

        fetchDevices();
    }, [userId]);

    useEffect(() => {
        if (stompClient) {
            const subscription = stompClient.subscribe('/topic/deviceId', (message) => {
                const notification = JSON.parse(message.body);
                if (devices.some(device => device.id === notification.deviceId)) {
                    alert(`Alerta: Consumul de energie pentru dispozitivul ${notification.deviceId} a depasit limita. Consumul TOTAL pe ora: ${notification.suma}, Consumul MAXIM permis pe ora: ${notification.maxHourlyEnergyConsumption}`);
                }
            });

            return () => {
                subscription.unsubscribe();
            };
        }
    }, [stompClient, devices]);

    // useEffect(() => {
    //     if (stompClient) {
    //         const subscription = stompClient.subscribe(`/topic/client/${userId}`, (message) => {
    //             const receivedMessage = JSON.parse(message.body);
    //             setIncomingMessages(prevMessages => [...prevMessages, receivedMessage]);
    //         });
    //
    //         return () => {
    //             subscription.unsubscribe();
    //         };
    //     }
    // }, [stompClient, userId]);
    // useEffect(() => {
    //     if (stompClient) {
    //         const subscription = stompClient.subscribe(`/topic/user-typing/${userId}`, (message) => {
    //             const { typing } = JSON.parse(message.body);
    //             setIsOtherUserTyping(typing);
    //         });
    //
    //         return () => {
    //             subscription.unsubscribe();
    //         };
    //     }
    // }, [stompClient, userId]);



    return (
        <div style={backgroundStyleClient}>
            <div className="titlu-client-page">You own the following devices:</div>
            {<Table devices={devices}/>}
            <div className="container-calendar">
                <div className="label-calendar">
                    Selectati data pentru care sa se afiseze istoricul consumului de energie:
                </div>
                <input
                    type="date"
                    id="datePicker"
                    value={selectedDate.toISOString().split('T')[0]}
                    onChange={handleDateChange}
                    className="custom-calendar"
                />
                <div className="container-buton-calendar">
                    <button type="button" className="custom-button-calendar" onClick={sendDateFromCalendar}>Submit
                    </button>
                </div>
            </div>
            <div className="custom-container-graph1">
                <EnergyConsumptionChart graphicData={graphicData} chartType={"bar"}/>
            </div>
            {/*<div className="custom-container-graph2">*/}
            {/*    <EnergyConsumptionChart graphicData={graphicData} chartType={"line"}/>*/}
            {/*</div>*/}
            {/* Componenta de Chat */}
            <div className="titlu-chatbox">Chat with the Administrator</div>
            <div className="chat-box">
                <ChatBox userId={userId} />
            </div>
        </div>
    );
}

export default Client;


