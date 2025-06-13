import React, {useState, useEffect} from 'react';
import InsertUserForm from './components/InsertUserForm';
import InsertDeviceForm from './components/InsertDeviceForm';
import UpdateUserForm from './components/UpdateUserForm';
import UpdateDeviceForm from './components/UpdateDeviceForm';
import UserTable from './components/UserTable';
import DeviceTable from './components/DeviceTable';
import './administrator.css';
import {jwtDecode as jwt_decode} from "jwt-decode";
import NotificationBox from './components/NotificationBox';
import {useContext} from "react";
import WebSocketContext from "../WebSocketContext";
import ChatForm from "./components/ChatForm";


const backgroundStyle = {
    backgroundPosition: 'center',
    backgroundSize: 'cover',
    backgroundRepeat: 'no-repeat',
    width: "100%",
    height: "100vh", //100% din inaltimea ferestrei
};

function AdminPage() {
    const [isInsertFormVisible, setInsertFormVisible] = useState(false);
    const [isUpdateFormVisible, setUpdateFormVisible] = useState(false);
    const [isUpdateDeviceFormVisible, setUpdateDeviceFormVisible] = useState(false);
    const [isInsertDeviceFormVisible, setInsertDeviceFormVisible] = useState(false);
    const [users, setUsers] = useState([]);
    const [devices, setDevices] = useState([]);
    const [currentUser, setCurrentUser] = useState([]);
    const [currentDevice, setCurrentDevice] = useState([]);
    const [currentDeviceOwner, setCurrentDeviceOwner] = useState([]);
    const [devicesWithUsers, setDevicesWithUsers] = useState([]);

    const stompClient = useContext(WebSocketContext);
    const [notification, setNotification] = useState([]);
    const [activeChats, setActiveChats] = useState({});

    const authToken = localStorage.getItem('authToken'); //preiau tokenul de autentificare din local storage

    useEffect(() => {
        async function fetchUsers() {
            try {

                const response = await fetch('http://localhost:8080/api/v1/auth/user', {
                    headers:{
                        'Authorization': `Bearer ${authToken}`,
                    },
                });

                if (!response.ok) {
                    throw new Error('Eroare la preluarea datelor de pe server');
                }

                const data = await response.json();
                setUsers(data);
            } catch (error) {
                console.error('A aparut o eroare la preluarea utilizatorilor:', error);
            }
        }

        fetchUsers();
    }, []);


    useEffect(() => {
        async function fetchDevices() {
            try {
                const response = await fetch('http://localhost:8081/api/v1/auth/device', {
                    headers:{
                        'Authorization': `Bearer ${authToken}`,
                    },
                });

                if (!response.ok) {
                    throw new Error('Eroare la preluarea datelor de pe server');
                }

                const dataDevices = await response.json();
                console.log("Primul device: ", dataDevices[0]);
                setDevices(dataDevices);
            } catch (error) {
                console.error('A aparut o eroare la preluarea device-urilor:', error);
            }
        }

        fetchDevices();
    }, []);


    async function deleteUser(userId) {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/auth/user/${userId}`, {
                method: 'DELETE',
                headers:{
                    'Authorization': `Bearer ${authToken}`,
                },
            });

            if (!response.ok) {
                throw new Error('Eroare la ștergerea utilizatorului');
            }

            // Actualizăm lista locala de utilizatori după ștergere
            const updatedUsers = users.filter(user => user.id !== userId);
            setUsers(updatedUsers);
        } catch (error) {
            console.error('A apărut o eroare:', error);
        }

        //sterg acest userId si din baza de date Devices, din tabelul userId
        try {
            const response = await fetch(`http://localhost:8081/api/v1/auth/user/${userId}`, {
                method: 'DELETE',
                headers:{
                    'Authorization': `Bearer ${authToken}`,
                },
            });

            if (!response.ok) {
                throw new Error('Eroare la ștergerea utilizatorului');
            }

            const updatedUsers = users.filter(user => user.id !== userId);
            setUsers(updatedUsers);
        } catch (error) {
            console.error('A apărut o eroare:', error);
        }
    }

    async function insertUser(insertedUser){
        let userId;

        try {
            const response = await fetch('http://localhost:8080/api/v1/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`,
                },
                body: JSON.stringify(insertedUser),
            });

            if (response.ok) {
                const responseData = await response.json(); //convertesc raspunsul in JSON
                const token = responseData.token;
                const decodedToken = jwt_decode(token);
                userId = decodedToken.userId;
                console.log("Id-ul userului inregistrat este: " + userId);
                console.log("Primul insert: " + responseData);

            } else {
                console.log("The insert operation failed!!!");
                return;
            }
        } catch (error) {
            console.error('Eroare la inserare: ', error);
            return;
        }

        //inserez id-ul user-ului si in Devices
        try {
            const response = await fetch(`http://localhost:8081/api/v1/auth/user/${userId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`,
                },
                body: JSON.stringify({id:userId}),
            });

            if (response.ok) {
                const response2Data = await response.json();
                console.log("Al 2-lea insert: " + response2Data);
            } else {
                console.log("The insert operation failed!!!");
            }
        } catch (error) {
            console.error('Eroare la inserare: ', error);
        }
    }



    async function updateUser(updatedUser) {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/auth/user/${updatedUser.id}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`,
                },
                body: JSON.stringify(updatedUser),
            });

            if (!response.ok) {
                throw new Error('Eroare la actualizarea utilizatorului');
            }

            const returnedUser = await response.json();
            const updatedUsersList = users.map(user =>
                user.id === returnedUser.id ? returnedUser : user
            );
            setUsers(updatedUsersList);

            setUpdateFormVisible(false);
            setCurrentUser(null);
        } catch (error) {
            console.error('A apărut o eroare:', error);
        }
    }


    function handleUpdate(user) {
        setCurrentUser(user);
        setUpdateFormVisible(true); //face formul vizibil cand apas pe update
    }

    function handleUpdateDevice(device) {
        setCurrentDevice(device);
        setUpdateDeviceFormVisible(true); //face formul vizibil cand apas pe update
    }

    function handleAddDevice(user) {
        setCurrentDeviceOwner(user);
        setInsertDeviceFormVisible(true);
    }

    async function deleteDevice(deviceId) {
        try {
            const response = await fetch(`http://localhost:8081/api/v1/auth/device/${deviceId}`, {
                method: 'DELETE',
                headers:{
                    'Authorization': `Bearer ${authToken}`,
                },

            });

            if (!response.ok) {
                throw new Error('Eroare la ștergerea device-ului');
            }

            // Actualizăm lista locala de utilizatori după ștergere
            const updatedDevices = devices.filter(device => device.id !== deviceId);
            setDevices(updatedDevices);
        } catch (error) {
            console.error('A apărut o eroare:', error);
        }

    }

    async function insertDevice(insertedDevice){
        //console.log(currentDeviceOwner.username);
        console.log(insertedDevice);
        try {
            const response = await fetch(`http://localhost:8081/api/v1/auth/device/${currentDeviceOwner.id}`, {
                //mode: 'same-origin',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`,
                },
                body: JSON.stringify(insertedDevice),
            });

            if (response.ok) {

                const responseDataDevices = await response.json(); //convertesc raspunsul in JSON
                console.log(responseDataDevices);

                const updatedDevicesList = devices.map(device =>
                    device.id === responseDataDevices.id ? responseDataDevices : device
                );
                setDevices(updatedDevicesList);

            } else {
                console.log("The insert operation failed!!!");
            }
        } catch (error) {
            console.error('Eroare la inserare: ', error);
        }
    }

    async function updateDevice(updatedDevice) {
        try {
            const response = await fetch(`http://localhost:8081/api/v1/auth/device/${updatedDevice.id}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`,
                },
                body: JSON.stringify(updatedDevice),
            });

            if (!response.ok) {
                throw new Error('Eroare la actualizarea device-ului');
            }

            const returnedDevice = await response.json();
            const deviceUser = users.find(user => user.id === returnedDevice.user_id);
            if (deviceUser) {
                returnedDevice.user = deviceUser;
            }

            const updatedDevicesList = devices.map(device =>
                device.id === returnedDevice.id ? returnedDevice : device
            );
            setDevices(updatedDevicesList);

            setUpdateDeviceFormVisible(false);
            setCurrentDevice(null);
        } catch (error) {
            console.error('A apărut o eroare:', error);
        }
    }

    useEffect(() => {
        setDevicesWithUsers(getDevicesWithUsers());
        console.log('Devices cu utilizatori:', getDevicesWithUsers());
    }, [devices, users]);

    function getDevicesWithUsers() {
        const devicesWithUsers = devices.map(device => {
            const deviceUser = users.find(user => user.id === device.user_id);

            // Dacă deviceUser este undefined, returnez device fara user asociat
            if(!deviceUser) {
                return device;
            }

            return {...device, user: deviceUser};
        });

        // Sorteaz array-ul în functie de numele utilizatorului
        return devicesWithUsers.sort((a, b) => {
            // Dacă a.user sau b.user este undefined, tratează cazul
            if(!a.user || !b.user) {
                return 0; // sau alte reguli, în funcție de necesități
            }

            if (a.user.fullname < b.user.fullname) return -1;
            if (a.user.fullname > b.user.fullname) return 1;
            return 0;
        });
    }

    // useEffect(() => {
    //     if (stompClient) {
    //         const subscription = stompClient.subscribe('/topic/admin', (message) => {
    //             const receivedMessage = JSON.parse(message.body);
    //             console.log(message);
    //             //const newNotification = `Ati primit un mesaj de la utilizatorul: ${receivedMessage.userId}, cu textul: ${receivedMessage.text}`;
    //             //setNotification(prevMessages => [...prevMessages, newNotification]);
    //
    //             // Caut utilizatorul in lista de utilizatori curenti
    //             const user = users.find(u => u.id === receivedMessage.userId);
    //             if (user) {
    //                 const newNotification = `Ati primit un mesaj de la utilizatorul: ${user.fullname} (${user.username}), cu textul: ${receivedMessage.text}`;
    //                 setNotification(prevNotifications => [...prevNotifications, newNotification]);
    //             } else {
    //                 const newNotification = `Ati primit un mesaj de la un utilizator necunoscut cu ID-ul: ${receivedMessage.userId}, cu textul: ${receivedMessage.text}`;
    //                 setNotification(prevNotifications => [...prevNotifications, newNotification]);
    //             }
    //         });
    //
    //         return () => {
    //             subscription.unsubscribe();
    //         };
    //     }
    // }, [stompClient]);

    useEffect(() => {
        if (stompClient) {
            const subscription = stompClient.subscribe('/topic/admin', (message) => {
                const receivedMessage = JSON.parse(message.body);

                setActiveChats(prevChats => {
                    const chatIsOpen = prevChats[receivedMessage.userId]?.isVisible; ///CAND PRIMESC MESAJELE DAU SEEN LA CLIENT
                    if (chatIsOpen) {
                        const readNotification = { userId: receivedMessage.userId, seen: true };
                        stompClient.send("/app/mark-message-as-read-admin-to-client", {}, JSON.stringify(readNotification));
                    }

                    if (prevChats[receivedMessage.userId]) {
                        return {
                            ...prevChats,
                            [receivedMessage.userId]: {
                                ...prevChats[receivedMessage.userId],
                                messages: [...prevChats[receivedMessage.userId].messages, { text: receivedMessage.text, sentByMe: false }]
                            }
                        };
                    } else {

                        return prevChats;
                    }
                });

                const user = users.find(u => u.id === receivedMessage.userId);
                if (user) {
                    const newNotification = `Ati primit un mesaj de la utilizatorul: ${user.fullname} (${user.username}), cu textul: ${receivedMessage.text}`;
                    setNotification(prevNotifications => [...prevNotifications, newNotification]);
                } else {
                    const newNotification = `Ati primit un mesaj de la un utilizator necunoscut cu ID-ul: ${receivedMessage.userId}, cu textul: ${receivedMessage.text}`;
                    setNotification(prevNotifications => [...prevNotifications, newNotification]);
                }
            });

            return () => {
                subscription.unsubscribe();
            };
        }
    }, [stompClient, activeChats]);



    // Funcție pentru a deschide o nouă conversație
    const handleChatButtonClick = (user) => {
        // Verificăm dacă chatul cu acest user este deja deschis
        if (!activeChats[user.id]) {
            setActiveChats(prevChats => ({
                ...prevChats,
                [user.id]: {
                    user: {
                        id: user.id,
                        name: user.fullname,
                        username: user.username
                    },
                    messages: [],
                    isVisible: true
                }
            }));
        } else {
            // Dacă chatul este deja deschis, doar comutăm vizibilitatea
            setActiveChats(prevChats => ({
                ...prevChats,
                [user.id]: {
                    ...prevChats[user.id],
                    isVisible: !prevChats[user.id].isVisible
                }
            }));
        }
    };

    // Funcție pentru a închide conversația
    const closeChat = (userId) => {
        // În loc să ștergem chatul, doar îl ascundem
        setActiveChats(prevChats => ({
            ...prevChats,
            [userId]: {
                ...prevChats[userId],
                isVisible: false
            }
        }));
    };

    const sendMessage = async (userId, message) => {
        if (stompClient) {
            const chatMessage = { text: message, isSentByCurrentUser: true, userId: userId };
            stompClient.send("/app/send-message-to-client", {}, JSON.stringify(chatMessage));

        }
        // Actualizăm starea după trimiterea mesajului
        setActiveChats(prevChats => ({
            ...prevChats,
            [userId]: {
                ...prevChats[userId],
                messages: [...prevChats[userId].messages, { text: message, sentByMe: true }]
            }
        }));
    };

    const updateChatMessages = (userId, updatedMessages) => {
        setActiveChats(prevChats => ({
            ...prevChats,
            [userId]: {
                ...prevChats[userId],
                messages: updatedMessages
            }
        }));
    };


    return (
        <div style={backgroundStyle}>
            <div className="titlu-admin-page">Welcome on the Administration page!</div>
            <div className="titlu-form1">Operations on user accounts</div>
            <div className="insert-button">
                <button type="button" className="custom-insert-button"
                        onClick={() => setInsertFormVisible(true)}>Click here to ADD a new USER
                </button>
                <InsertUserForm
                    visible={isInsertFormVisible}
                    onClose={() => setInsertFormVisible(false)}
                    onInsert={insertUser}
                />
            </div>
            <UpdateUserForm
                visible={isUpdateFormVisible} //visible prop
                onClose={() => { setUpdateFormVisible(false); setCurrentUser(null); }} //onClose prop
                user={currentUser} //user prop
                onUpdate={updateUser}
            />
            {/*<ChatForm*/}
            {/*    visible={isChatFormVisible}*/}
            {/*    onClose={() => { setChatFormVisible(false); setCurrentUser(null); }}*/}
            {/*    user={currentUser}*/}
            {/*    onChat={chatWithUser}*/}
            {/*    messages={}*/}
            {/*/>*/}
            {Object.entries(activeChats).map(([userId, chatDetails]) => (
                <ChatForm
                    key={userId}
                    user={chatDetails.user}
                    messages={chatDetails.messages}
                    onSendMessage={(userId, messageText) => sendMessage(userId, messageText)}
                    onClose={() => closeChat(userId)}
                    isVisible={chatDetails.isVisible}
                    updateMessages={(updatedMessages) => updateChatMessages(userId, updatedMessages)}
                />
            ))}
            <UserTable users={users} onDelete={deleteUser} onUpdate={handleUpdate} onAddDevice={handleAddDevice} onChat={handleChatButtonClick}/>
            <div className="titlu-form2">Operations on smart energy metering devices</div>
            <InsertDeviceForm
                visible={isInsertDeviceFormVisible}
                onClose={() => setInsertDeviceFormVisible(false)}
                onInsertDevice={insertDevice}
            />
            <UpdateDeviceForm
                visible={isUpdateDeviceFormVisible} //visible prop
                onClose={() => { setUpdateDeviceFormVisible(false); setCurrentDevice(null); }} //onClose prop
                device={currentDevice} //device prop
                onUpdateDevice={updateDevice}
            />
            <DeviceTable devices={devicesWithUsers} onDeleteDevice={deleteDevice} onUpdateDevice={handleUpdateDevice}/>
            <div className="titlu-chatbox-administrator">Notifications from the clients</div>
            <div className="notification-box-administrator-container">
                <div className="notification-box-administrator">
                    <NotificationBox messages={notification}/>
                </div>
            </div>
        </div>
    );
}

export default AdminPage;
