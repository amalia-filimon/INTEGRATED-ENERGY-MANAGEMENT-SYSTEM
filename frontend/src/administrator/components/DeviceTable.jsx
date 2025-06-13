import React, {useEffect} from 'react';
import './UserTable.css';

function DeviceTable({ devices, onDeleteDevice, onUpdateDevice }) {

    useEffect(() => {
        console.log('DeviceTable se re-renderează.');
    });


    return (
        <table className="users-table">
            <thead>
            <tr className="users-table-header">
                <th>ID</th>
                <th>Owner</th>
                <th>Description</th>
                <th>Address</th>
                <th>Maximum hourly energy consumption</th>
                <th>Delete</th> {/* coloana noua pt butonul de delete */}
                <th>Edit</th> {/* coloana noua pt butonul de update */}
            </tr>
            </thead>
            <tbody>
            {devices
                .filter(device => device.user)  // Aceasta linie va elimina dispozitivele fara user
                .map(device => (
                    <tr key={device.id}>
                        <td>{device.id}</td>
                        <td>{device.user.username}</td>
                        <td>{device.description}</td>
                        <td>{device.address}</td>
                        <td>{device.maxHourlyEnergyConsumption}</td>
                        <td>
                            <button className="delete-button" onClick={() => onDeleteDevice(device.id)}>DELETE</button> {/* Folosim onDelete aici */}
                        </td>
                        <td>
                            <button className="update-button" onClick={() => onUpdateDevice(device)}>UPDATE</button> {/* Folosim onUpdate aici */}
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}

export default DeviceTable;
