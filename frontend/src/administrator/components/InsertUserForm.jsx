import React, {useState} from 'react';
import './InsertUserForm.css';

function InsertUserForm({ visible, onClose, onInsert }) {

    const [fullname, setFullname] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [address, setAddress] = useState("");

    //ma asigur ca toate campurile sunt completate
    const validateForm = () => {
        return (
            fullname.trim() !== '' &&
            username.trim() !== '' &&
            password.trim() !== '' &&
            address.trim() !== ''
        );
    };

    const handleSubmit = async () => {

        //aici fac validarea
        if (!validateForm()) {
            alert('Please complete ALL the fields in order to get the user inserted!');
            return;
        }

        const data = {
            fullname: fullname,
            username: username,
            password: password,
            address: address
        };

        onInsert(data);

        //dupa inserare golesc campurile
        setFullname('');
        setUsername('');
        setPassword('');
        setAddress('');

        onClose();
    };


    return (
        <div>
            {visible && (
                <div className="custom-insert-form">
                    <form onSubmit={handleSubmit}>
                        <div className="input-container-userform">
                            <label>Full Name</label>
                            <input
                                type="text"
                                value={fullname}
                                onChange={(e) => setFullname(e.target.value)}
                                placeholder="What's your name?"
                                className="custom-input-userform" //am adaugat o clasa pt stilizare
                            />
                        </div>
                        <div className="input-container-userform">
                            <label>Username</label>
                            <input
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                placeholder="Username..."
                                className="custom-input-userform" //am adaugat o clasa pt stilizare
                            />
                        </div>
                        <div className="input-container-userform">
                            <label>Password</label>
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="Password..."
                                className="custom-input-userform" //am adaugat o clasa pt stilizare
                            />
                        </div>
                        <div className="input-container-userform">
                            <label>Address</label>
                            <input
                                type="address"
                                value={address}
                                onChange={(e) => setAddress(e.target.value)}
                                placeholder="Cluj, Zorilor, Str. Lalelelor 21..."
                                className="custom-input-userform" //am adaugat o clasa pt stilizare
                            />
                        </div>

                        <div className="button-container-userform">
                            <button type="button" className="custom-button-userform" onClick={handleSubmit}>Submit
                            </button>
                        </div>
                    </form>

                </div>
            )}
        </div>
    );
}

export default InsertUserForm;
