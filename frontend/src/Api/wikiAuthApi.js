const BASE_URL = process.env.REACT_APP_API_URL;

export const handleLoginSubmit = async (email, password) => {
    const data = {
        usernameOrEmail: email,
        password: password,
    }
    const response = await fetch(`${BASE_URL}/api/public/auth/login`, {
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
    const response = await fetch(`${BASE_URL}/api/public/auth/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
        });
    const responseData = await response.json();
    return responseData;
};