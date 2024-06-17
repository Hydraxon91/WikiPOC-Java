const BASE_URL = process.env.REACT_APP_API_URL;

export const handleLoginSubmit = async (email, password) => {
    const data = {
        email: email,
        password: password,
    }
    const response = await fetch(`${BASE_URL}/api/Auth/Login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
        });
    const responseData = await response.json();
    return responseData;
};

export const handleRegisterSubmit = async (email, username, password, role) => {
    const data = {
        email: email,
        username: username,
        password: password,
        role: role
    }
    const response = await fetch(`${BASE_URL}/api/Auth/Register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
        });
    const responseData = await response.json();
    return responseData;
};