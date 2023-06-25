import './../style/form.css'
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import {fetchRegisterData} from "../redux/slices/AuthReducer.js";
import {useAppDispatch} from "../redux/Store.js";


const Register = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        dispatch(fetchRegisterData({first_name: firstName, last_name: lastName, email: email, password: password}))
            .then((response) => {
                if (response.error) {
                    setErrors(response.payload.message);
                } else {
                    navigate("/");
                }
            });
    }

    return (
        <div className="container-form">
            <h1>Register</h1>
            <p className='error-paragraph'>{errors}</p>
            <form className='form' onSubmit={handleSubmit}>
                <input className='input' placeholder="First name" type="text" value={firstName}
                       onChange={(e) => setFirstName(e.target.value)}/>
                <input className='input' placeholder="Last name" type="text" value={lastName}
                       onChange={(e) => setLastName(e.target.value)}/>
                <input className='input' placeholder="Email" type="text" value={email}
                       onChange={(e) => setEmail(e.target.value)}/>
                <input className='input' placeholder="Password" type="password" value={password}
                       onChange={(e) => setPassword(e.target.value)}/>
                <button className='button-form' type="submit">Submit</button>
            </form>
        </div>
    );
}

export default Register;