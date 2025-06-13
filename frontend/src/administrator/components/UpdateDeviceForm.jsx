import React, {useState, useEffect} from 'react';
import './UpdateUserForm.css';

function UpdateDeviceForm({ visible, onClose, device, onUpdateDevice }) {

    const [description, setDescription] = useState("");
    const [address, setAddress] = useState("");
    const [maxHourlyEnergyConsumption, setMaxHourlyEnergyConsumption] = useState("");

    //ma asigur ca toate campurile sunt completate
    const validateForm = () => {
        return (
            description.trim() !== '' &&
            address.trim() !== '' &&
            maxHourlyEnergyConsumption.trim() !== ''
        );
    };

    useEffect(() => {
        if (device) {
            setDescription(device.description || "");
            setAddress(device.address || "");
            setMaxHourlyEnergyConsumption(device.maxHourlyEnergyConsumption || "");
        }
    }, [device]);

    const handleSubmit = async () => {

        //aici fac validarea
        if (!validateForm()) {
            alert('Please complete ALL the fields in order to get the device updated!');
            return;
        }

        const data = {
            id: device.id,
            description: description,
            address: address,
            maxHourlyEnergyConsumption: maxHourlyEnergyConsumption
        };

        onUpdateDevice(data); // Aici utilizez funcția de actualizare pe care am trimis o ca prop din admin
        onClose(); //inchid formularul
    };


    return (
        <div>
            {visible && (
                <div className="custom-update-form">
                    <form onSubmit={handleSubmit}>
                        <div className="input-container-updateform">
                            <label>Description</label>
                            <input
                                type="text"
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                className="custom-input-updateform" //am adaugat o clasa pt stilizare
                            />
                        </div>
                        <div className="input-container-updateform">
                            <label>Address</label>
                            <input
                                type="text"
                                value={address}
                                onChange={(e) => setAddress(e.target.value)}
                                className="custom-input-updateform" //am adaugat o clasa pt stilizare
                            />
                        </div>
                        <div className="input-container-updateform">
                            <label>Maximum Hourly Energy Consumption</label>
                            <input
                                type="text"
                                value={maxHourlyEnergyConsumption}
                                onChange={(e) => setMaxHourlyEnergyConsumption(e.target.value)}
                                className="custom-input-updateform" //am adaugat o clasa pt stilizare
                            />
                        </div>
                        <div className="button-container-updateform">
                            <button type="button" className="custom-button-updateform" onClick={handleSubmit}>Submit
                            </button>
                        </div>
                    </form>

                </div>
            )}
        </div>
    );
}

export default UpdateDeviceForm;
