import React, {useState, useEffect} from 'react';
import './UpdateUserForm.css';

function UpdateUserForm({ visible, onClose, user, onUpdate }) {

    const [fullname, setFullname] = useState("");
    const [username, setUsername] = useState("");
    const [address, setAddress] = useState("");

    //ma asigur ca toate campurile sunt completate
    const validateForm = () => {
        return (
            fullname.trim() !== '' &&
            username.trim() !== '' &&
            address.trim() !== ''
        );
    };

    useEffect(() => {
        if (user) {
            setFullname(user.fullname || "");
            setUsername(user.username || "");
            setAddress(user.address || "");
        }
    }, [user]);

    const handleSubmit = async () => {

        //aici fac validarea
        if (!validateForm()) {
            alert('Please complete ALL the fields in order to get the user updated!');
            return;
        }

        const data = {
            id: user.id,
            fullname: fullname,
            username: username,
            address: address
        };

        onUpdate(data); // Aici utilizez funcția de actualizare pe care am trimis o ca prop din admin
        onClose(); //inchid formularul
    };


    return (
        <div>
            {visible && (
                <div className="custom-update-form">
                    <form onSubmit={handleSubmit}>
                        <div className="input-container-updateform">
                            <label>Full Name</label>
                            <input
                                type="text"
                                value={fullname}
                                onChange={(e) => setFullname(e.target.value)}
                                className="custom-input-updateform" //am adaugat o clasa pt stilizare
                            />
                        </div>
                        <div className="input-container-updateform">
                            <label>Username</label>
                            <input
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                className="custom-input-updateform" //am adaugat o clasa pt stilizare
                            />
                        </div>
                        <div className="input-container-updateform">
                            <label>Address</label>
                            <input
                                type="address"
                                value={address}
                                onChange={(e) => setAddress(e.target.value)}
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

export default UpdateUserForm;
