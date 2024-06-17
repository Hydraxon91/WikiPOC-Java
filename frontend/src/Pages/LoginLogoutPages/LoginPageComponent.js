import { useState, useEffect } from "react"
import "../../Styles/login.css";
import { jwtDecode } from 'jwt-decode';
import { Link, useNavigate } from 'react-router-dom';
import SuccessfullElement from "./SuccessfullElement";
import { handleLoginSubmit } from "../../Api/wikiAuthApi";

export default function LoginPageComponent({handleLogin}){
    //console.log(user);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [response, setResponse] = useState(null);
    const [emailInputClass, setEmailInputClass] = useState('login-inputbox');
    const [passwordInputClass, setPasswordInputClass] = useState('login-inputbox');
    const [showSuccessMessage, setShowSuccessMessage] = useState(false);
    const navigate = useNavigate();
    
    const login = (jwt_token) => {
        const decoded = jwtDecode(jwt_token);
        // setUser(decoded);
        const expirationTimestamp = parseInt(decoded.exp, 10);
        const expirationDate = new Date(expirationTimestamp * 1000)
        // cookies.set("jwt_authorization", jwt_token, {expires: expirationDate});
        handleLogin(jwt_token, expirationDate);
    }

    const HandleSubmit = () => {
        handleLoginSubmit(email, password)
            .then(response => {
                // Handle the response here
                setResponse(response);
            })
            .catch(error => {
                // Handle errors here
                console.error(error);
            });
    }

    const InputClick = () => {
        setEmailInputClass('login-inputbox');
        setPasswordInputClass('login-inputbox');
    }

    useEffect(()=>{

    },[emailInputClass, passwordInputClass])
    
    useEffect(()=>{
        if (response) {
            console.log(response)
            if (response?.token) {
                login(response.token);
                alert('Succesfully logged in!');
                navigate('/');
            }
            else{
                if (response['Bad credentials'][0] === 'Invalid email' || 'Invalid Username') {

                        setEmailInputClass('login-inputbox wrong-credential');
                }
                else {
                    setPasswordInputClass('login-inputbox wrong-credential');
                }
            }
        }
        
    },[response])

    return(
        <div className='login-form'>
            {showSuccessMessage && (
                <>
                    <SuccessfullElement message={"Successfully logged in"}/>
                    <div className="login-successoverlay" />
                </>
            )}
            <div>
                <form>
                    <h2 className="login-text">Login</h2>
                    <div className="login-inputboxholder">
                        <div className={emailInputClass}>
                            <input type="text" required onClick={InputClick} onChange={(e) => setEmail(e.target.value)}></input>
                            <label for="emailInput">Email/Username</label>
                            <h3 className="invalid-email-text">invalid email or username</h3>
                        </div>
                        <div className={passwordInputClass}>
                            <input type="password" required onClick={InputClick} onChange={(e) => setPassword(e.target.value)}></input>
                            <label for="">Password</label>
                            <h3 className="invalid-password-text">invalid password</h3>
                        </div>
                    </div>
                    <div className="forget">
                            <a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ"> Forgot Password?</a>
                    </div>
                    <button type="button" className="login-button" onClick={HandleSubmit}>Login</button>
                    <div className="register">
                        <p>
                            Don't have an account? 
                            <Link to="/register">
                                <a> Register here!</a>
                            </Link>
                        </p>
                    </div>
                </form>
            </div>
        </div>
    )
}