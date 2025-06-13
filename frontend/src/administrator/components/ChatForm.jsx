import React, {useContext, useEffect, useState} from 'react';
import Draggable from 'react-draggable';
import './ChatForm.css';
import WebSocketContext from "../../WebSocketContext";

function ChatForm({ user, messages, onClose, onSendMessage, isVisible, updateMessages }) {
    const [message, setMessage] = useState('');
    const [isUserTyping, setIsUserTyping] = useState(false);
    const stompClient = useContext(WebSocketContext);

    //functionalitatea de seen admin-to-client
    useEffect(() => {
        if (isVisible) {
            // Conversatia a fost deschisa, trimite solicitarea "seen" la server
            if (stompClient && user.id) {
                const readNotification = { userId: user.id, seen: true };
                stompClient.send("/app/mark-message-as-read-admin-to-client", {}, JSON.stringify(readNotification));
            }
        }
    }, [isVisible, stompClient, user.id]);


    useEffect(() => {
        if (stompClient) {
            const seenSubscription = stompClient.subscribe(`/topic/message-read-in-admin/${user.id}`, (message) => {
                const { seen } = JSON.parse(message.body);
                if (seen) {
                    markMessagesAsSeen();
                }
            });

            return () => {
                seenSubscription.unsubscribe();
            };
        }
    }, [stompClient, user.id, messages]);

    const markMessagesAsSeen = () => {
        const updatedMessages = messages.map((msg, index) => {
            if (msg.sentByMe && index === messages.length - 1) {
                return { ...msg, seen: true };
            }
            return msg;
        });

        updateMessages(updatedMessages);
    };



    //functionalitatea de typing
    useEffect(() => {
        const typingSubscription = stompClient.subscribe(`/topic/user-typing/${user.id}`, (message) => {
            const { typing } = JSON.parse(message.body);
            if (typing === "true") {
                setIsUserTyping(true);
            } else if (typing === "false") {
                setIsUserTyping(false);
            }


        });

        return () => {
            typingSubscription.unsubscribe();
        };
    }, [stompClient, user.id]);


    const handleSubmit = () => {
        onSendMessage(user.id, message);
        setMessage('');
    };

    if (!isVisible) return null;


    let typingTimeout;

    const handleAdminTyping = (typing) => {
        const typingMessage = {userId: user.id, typing}
        stompClient.send("/app/admin-typing", {}, JSON.stringify(typingMessage));
    };

    const handleMessageChange = (event) => {
        setMessage(event.target.value);
        handleAdminTyping(true);
        clearTimeout(typingTimeout);
        typingTimeout = setTimeout(() => handleAdminTyping(false), 3000);
    };



    return (
        <Draggable>
            <div className="chat-form">
                <div className="chat-header">
                    Chat with {user.name} ({user.username})
                    <button onClick={() => onClose(user.id)}>Close</button>
                </div>
                {/*<ul className="messages-list">*/}
                {/*    {messages.map((m, index) => <li key={index}>{m}</li>)}*/}
                {/*</ul>*/}
                <ul className="messages-list">
                    {/*{messages.map((msg, index) => (*/}
                    {/*    <li key={index} className={msg.sentByMe ? 'message-sent' : 'message-received'}>*/}
                    {/*        {msg.text}*/}
                    {/*    </li>*/}
                    {/*))}*/}
                    {messages.map((msg, index) => (
                        <li key={index} className={msg.sentByMe ? 'message-sent' : 'message-received'}>
                            {msg.text}
                            {msg.seen && <div className="seen-indicator">Seen</div>}
                        </li>
                    ))}
                </ul>
                {
                    isUserTyping && <div className="typing-indicator">{user.username} is typing...</div>
                }
                <input
                    value={message}
                    // onChange={(e) => setMessage(e.target.value)}
                    onChange={handleMessageChange}
                    type="text"
                    placeholder="Type your message..."
                />
                <button onClick={handleSubmit}>Send</button>
            </div>
        </Draggable>
    );
}

export default ChatForm;