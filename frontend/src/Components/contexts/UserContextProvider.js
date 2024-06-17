import React, { createContext, useContext, useState } from 'react';

const UserContext = createContext(null);

export const UserContextProvider = ({ children }) => {
  const [decodedTokenContext, setDecodedTokenContext] = useState(null);
  const updateUser = (newUser) => {
    setDecodedTokenContext(newUser);
  };

  return (
    <UserContext.Provider value={{ decodedTokenContext, updateUser }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUserContext = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUserContext must be used within a UserContextProvider');
  }
  return context;
};