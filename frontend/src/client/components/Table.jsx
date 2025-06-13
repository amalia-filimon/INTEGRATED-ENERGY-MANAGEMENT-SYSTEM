
import './Table.css';
import React from "react";

function Table({ devices}) {

    return (
        <table className="client-table">
            <thead>
            <tr className="client-table-header">
                <th>ID</th>
                <th>Description</th>
                <th>Address</th>
                <th>Maximum hourly energy consumption</th>
            </tr>
            </thead>
            <tbody>
            {devices
                .map(device => (
                    <tr key={device.id}>
                        <td>{device.id}</td>
                        <td>{device.description}</td>
                        <td>{device.address}</td>
                        <td>{device.maxHourlyEnergyConsumption}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}

export default Table;
