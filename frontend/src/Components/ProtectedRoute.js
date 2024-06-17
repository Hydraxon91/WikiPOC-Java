import React from 'react';
import { Navigate } from 'react-router-dom';
import { useUserContext } from './contexts/UserContextProvider';

const ProtectedRoute = ({ roles, children }) => {
    const { decodedTokenContext } = useUserContext();
    const userRoles = decodedTokenContext?.['http://schemas.microsoft.com/ws/2008/06/identity/claims/role'];
    const isAuthorized = () => {
        if (!decodedTokenContext) {
            return false;
        }
        if (!roles || roles.length === 0) {
            return true;
        }
        if (Array.isArray(userRoles)) {
            console.log(userRoles);
            
            // return roles.some(role => userRoles.includes(role));
        }
        return roles.includes(userRoles);
    };

    if (isAuthorized()) {
        return children
    } else {
        return <Navigate to="/" />;
    }
};

export default ProtectedRoute;
