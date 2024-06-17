import { useState, useEffect } from 'react';

const WikiStyles = () => {
  const [styles, setStyles] = useState({
    logo: '/img/logo.png',
    wikiName: 'Your Wiki',
    bodyColor: '#507ced',
    articleRightColor: '#3c5fb8',
    articleRightInnerColor: '#2b4ea6',
    articleColor: '#526cad',
    footerListLinkTextColor: '#1d305e',
    footerListTextColor: '#233a71',
  });

  useEffect(() => {
    //console.log(styles);
  }, [styles]);

  const updateStyles = (newStyles) => {
    // console.log(newStyles);
    // setStyles(newStyles);
    setStyles((prevStyles) => ({ ...prevStyles, ...newStyles }));
    //console.log("style updated");
  };

  return { styles, updateStyles };
};

export default WikiStyles;
