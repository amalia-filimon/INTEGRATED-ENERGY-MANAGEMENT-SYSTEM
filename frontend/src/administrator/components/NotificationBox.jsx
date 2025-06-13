import React from 'react';
import './NotificationBox.css';

function NotificationBox({ messages }) {
    return (
        <div className="notification-box">
            {messages.map((message, index) => (
                <div key={index} className="notification-message">
                    {message}
                </div>
            ))}
        </div>
    );
}

export default NotificationBox;
