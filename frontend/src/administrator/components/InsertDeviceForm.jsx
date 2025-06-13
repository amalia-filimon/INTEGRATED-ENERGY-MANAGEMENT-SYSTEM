import React, {useState} from 'react';
import './InsertDeviceForm.css';

function InsertDeviceForm({ visible, onClose, onInsertDevice }) {

    const [description, setDescription] = useState("");
    const [address, setAddress] = useState("");
    const [maxHourlyEnergyConsumption, setmaxHourlyEnergyConsumption] = useState("");


    //ma asigur ca toate campurile sunt completate
    const validateForm = () => {
        return (
            description.trim() !== '' &&
            address.trim() !== '' &&
            maxHourlyEnergyConsumption.trim() !== ''
        );
    };

    const handleSubmit = async () => {
        //aici fac validarea
        if (!validateForm()) {
            alert('Please complete ALL the fields in order to get the device inserted!');
            return;
        }

        const data = {
            description: description,
            address: address,
            maxHourlyEnergyConsumption: maxHourlyEnergyConsumption
        };

        onInsertDevice(data);

        //dupa inserare golesc campurile
        setDescription('');
        setAddress('');
        setmaxHourlyEnergyConsumption('');

        onClose();
    };


    return (
        <div>
            {visible && (
                <div className="custom-insertdevice-form">
                    <form onSubmit={handleSubmit}>
                        <div className="input-container-deviceform">
                            <label>Description</label>
                            <input
                                type="text"
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                placeholder="Enter the model, connectivity features..."
                                className="custom-input-deviceform" //am adaugat o clasa pt stilizare
                            />
                        </div>
                        <div className="input-container-deviceform">
                            <label>Address</label>
                            <input
                                type="address"
                                value={address}
                                onChange={(e) => setAddress(e.target.value)}
                                placeholder="Cluj, Zorilor, Str. Lalelelor 21..."
                                className="custom-input-deviceform" //am adaugat o clasa pt stilizare
                            />
                        </div>
                        <div className="input-container-deviceform">
                            <label>Maximum hourly energy consumption</label>
                            <input
                                type="text"
                                value={maxHourlyEnergyConsumption}
                                onChange={(e) => setmaxHourlyEnergyConsumption(e.target.value)}
                                placeholder="e.g. 50 kWh"
                                className="custom-input-deviceform" //am adaugat o clasa pt stilizare
                            />
                        </div>

                        <div className="button-container-deviceform">
                            <button type="button" className="custom-button-deviceform" onClick={handleSubmit}>Submit
                            </button>
                        </div>
                    </form>

                </div>
            )}
        </div>
    );
}

export default InsertDeviceForm;
