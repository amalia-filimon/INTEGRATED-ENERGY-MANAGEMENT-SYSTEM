import React from 'react';
import './UserTable.css';

function UserTable({ users, onDelete, onUpdate, onAddDevice, onChat }) {
    return (
        <table className="users-table">
            <thead>
            <tr className="users-table-header">
                <th>ID</th>
                <th>Full Name</th>
                <th>Username</th>
                <th>Address</th>
                <th>Delete</th> {/* coloana noua pt butonul de delete */}
                <th>Edit</th> {/* coloana noua pt butonul de update */}
                <th>Add Device</th> {/* coloana noua pt butonul de adaugare device */}
                <th>Chat</th> {/* coloana noua pt butonul de adaugare device */}
            </tr>
            </thead>
            <tbody>
            {users.map(user => (
                <tr key={user.id}>
                    <td>{user.id}</td>
                    <td>{user.fullname}</td>
                    <td>{user.username}</td>
                    <td>{user.address}</td>
                    <td>
                        <button className="delete-button" onClick={() => onDelete(user.id)}>DELETE</button> {/* Folosim onDelete aici */}
                    </td>
                    <td>
                        <button className="update-button" onClick={() => onUpdate(user)}>UPDATE</button> {/* Folosim onUpdate aici */}
                    </td>
                    <td>
                        <button className="delete-button" onClick={() => onAddDevice(user)}>ADD DEVICE</button> {/* Folosim onAddDevice aici */}
                    </td>
                    <td>
                        <button className="chat-button-user-table" onClick={() => onChat(user)}>CHAT</button> {/* Folosim onChat aici */}
                    </td>
                </tr>
            ))}
            </tbody>
        </table>
    );
}

export default UserTable;
