import './../style/form.css'
import {Link, useNavigate} from "react-router-dom";
import {useState} from "react";
import {fetchKeyPairData} from "../redux/slices/AuthReducer.js";
import {useAppDispatch} from "../redux/Store.js";


const Login = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        dispatch(fetchKeyPairData({email, password}))
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
            <h1>Login</h1>
            <p className='error-paragraph'>{errors}</p>
            <form className='form' onSubmit={handleSubmit}>
                <input className='input' placeholder="Email" type="text" value={email}
                       onChange={(e) => setEmail(e.target.value)}/>
                <input className='input' placeholder="Password" type="password" value={password}
                       onChange={(e) => setPassword(e.target.value)}/>
                <button className='button-form' type="submit">Submit</button>
                <h3 className='form-text'>Not yet registered? <Link className='link-form' to="/register">Click</Link>
                </h3>
            </form>

        </div>
    );
}

export default Login;