import React from 'react';
import { Navigate } from 'react-router-dom';
import { useUserContext } from './contexts/UserContextProvider';

const PublicRoute = ({ children }) => {
    const { decodedTokenContext } = useUserContext();

    if (!decodedTokenContext) {
        return children;
      } else {
        return <Navigate to="/" />;
      }
};

export default PublicRoute;
