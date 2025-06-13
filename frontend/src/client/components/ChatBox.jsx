import React, {useEffect, useState} from 'react';
import './ChatBox.css';
import {useContext} from "react";
import WebSocketContext from "../../WebSocketContext";

function ChatBox({userId}) {
    const [message, setMessage] = useState('');
    const [messages, setMessages] = useState([]);
    const [isAdminTyping, setIsAdminTyping] = useState(false);

    const stompClient = useContext(WebSocketContext);

    let typingTimeout;

    const handleUserTyping = (typing) => {
        // Trimite la server un eveniment de 'typing'
        stompClient.send("/app/user-typing", {}, JSON.stringify({userId, typing}));
    };

    const handleMessageChange = (event) => {
        setMessage(event.target.value);
        handleUserTyping(true);
        clearTimeout(typingTimeout);
        typingTimeout = setTimeout(() => handleUserTyping(false), 3000);
    };

    useEffect(() => {
        if (stompClient) {
            const subscription = stompClient.subscribe(`/topic/client/${userId}`, (message) => {
                const receivedMessage = JSON.parse(message.body);
                // Adaugă mesajul primit în lista de mesaje
                setMessages(prevMessages => [...prevMessages, {...receivedMessage, isSentByCurrentUser: false}]);

                // Trimit notificarea "seen" pentru acest mesaj
                const seenNotification = { userId: userId, seen: true };
                stompClient.send("/app/mark-message-as-read-client-to-admin", {}, JSON.stringify(seenNotification));
            });

            return () => {
                subscription.unsubscribe();
            };
        }
    }, [stompClient, userId]);

    const handleSendMessage = () => {
        const newMessage = {text: message, isSentByCurrentUser: true};
        setMessages([...messages, newMessage]);
        setMessage('');

        console.log(message);

        if (stompClient) {
            const chatMessage = {text: message, isSentByCurrentUser: true, userId: userId};
            stompClient.send("/app/send-message-to-admin", {}, JSON.stringify(chatMessage));
            setMessage('');
        }
    };

    useEffect(() => {
        if (stompClient) {
            const typingSubscription = stompClient.subscribe(`/topic/admin-typing/${userId}`, (message) => {
                const {typing} = JSON.parse(message.body);
                if (typing === "true") {
                    setIsAdminTyping(true);
                } else if (typing === "false") {
                    setIsAdminTyping(false);
                }


            });

            return () => {
                typingSubscription.unsubscribe();
            };
        }
    }, [stompClient, userId]);

    const updateMessagesAsSeen = () => {
        setMessages(prevMessages =>
            prevMessages.map((msg, index) =>
                index === prevMessages.length - 1 && msg.isSentByCurrentUser
                    ? {...msg, seen: true}
                    : msg
            )
        );
    };

    useEffect(() => {
        if (stompClient) {
            const subscription = stompClient.subscribe(`/topic/message-read-in-client/${userId}`, (message) => {
                const {seen} = JSON.parse(message.body);
                console.log(JSON.parse(message.body));
                if(seen === "true") {
                    //setLastMessageSeen(true);
                    updateMessagesAsSeen();
                }
            });

            return () => {
                subscription.unsubscribe();
            };
        }
    }, [stompClient, userId]);
    // useEffect(() => {
    //     if (stompClient) {
    //         const subscription = stompClient.subscribe(`/topic/message-read-in-client/${userId}`, (message) => {
    //             const {seen} = JSON.parse(message.body);
    //             if(seen === "true") {
    //                 updateMessagesAsSeen();
    //             }
    //         });
    //
    //         return () => {
    //             subscription.unsubscribe();
    //         };
    //     }
    // }, [stompClient, userId]);





    return (
        <div>
            {/*<div className="messages">*/}
            {/*    {messages.map((msg, index) => (*/}
            {/*        <div key={index} className={`message ${msg.isSentByCurrentUser ? 'sent' : 'received'}`}>*/}
            {/*            {msg.text}*/}
            {/*            {msg.seen && <div className="seen">Seen</div>}*/}
            {/*        </div>*/}
            {/*    ))}*/}
            {/*</div>*/}
            <div className="messages">
                {messages.map((msg, index) => (
                    <div key={index} className={`message ${msg.isSentByCurrentUser ? 'sent' : 'received'}`}>
                        {msg.text}
                        {msg.isSentByCurrentUser && msg.seen && index === messages.length - 1 && <div className="seen">Seen</div>}
                    </div>
                ))}
            </div>

            {isAdminTyping && <div className="admin-typing-indicator">Admin is typing...</div>}
            <input
                type="text"
                value={message}
                onChange={handleMessageChange}
                className="input-chatbox"
            />
            <button className="button-chatbox" onClick={handleSendMessage}>Send</button>
        </div>
    );
}

export default ChatBox;
