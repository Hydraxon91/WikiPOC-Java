import React, { createContext, useContext, useState, useEffect } from 'react';
import { fetchCurrentStyles, updateStyles } from '../../Api/wikiApi';
const StyleContext = createContext();

export const StyleProvider = ({ children }) => {
  const [styles, setStyles] = useState({
    logo: 'logo_pfp.png',
    wikiName: 'Your Wiki',
    bodyColor: '#507ced',
    articleRightColor: '#3c5fb8',
    articleRightInnerColor: '#2b4ea6',
    articleColor: '#526cad',
    footerListLinkTextColor: '#1d305e',
    footerListTextColor: '#233a71',
    fontFamily: 'Arial, sans-serif'
  });


  useEffect(() => {
    fetchCurrentStyles(setStyles);
  }, []);

  return (
    <StyleContext.Provider value={{ styles, updateStyles, setStyles }}>
      {children}
    </StyleContext.Provider>
  );
};

export const useStyleContext = () => {
    const context = useContext(StyleContext);
    if (!context) {
      throw new Error('useStyleContext must be used within a StyleProvider');
    }
    return context;
  };