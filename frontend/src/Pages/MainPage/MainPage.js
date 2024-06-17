import React, { useEffect, useState } from 'react';
import { Outlet, useLocation } from 'react-router-dom';
import WikiList from './Components/WikiList';
import Breadcrumbs from '../ForumPages/Components/Breadcrumbs';
import HeaderComponent from './Components/HeaderComponent';
import { useStyleContext } from '../../Components/contexts/StyleContext';
import { useUserContext } from '../../Components/contexts/UserContextProvider';
import { getWikiPageTitles } from '../../Api/wikiApi';

const MainPage = ({ decodedToken, handleLogout, cookies, setWikiPageTitles, categories }) => {
  const location = useLocation();
  const { styles }  = useStyleContext();
  const { updateUser } = useUserContext();
  const [userName, setUserName] = useState(null);
  const [userRole, setUserRole] = useState(null);

  useEffect(()=>{
    // console.log(decodedToken);
    if (decodedToken) {
      updateUser(decodedToken);
      setUserName(decodedToken["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"]);
      setUserRole("Role: " +decodedToken["http://schemas.microsoft.com/ws/2008/06/identity/claims/role"]);
    }
    else{
      setUserName(null)
      setUserRole(null);
    }
  }, [decodedToken])

  useEffect(() => {
    // Fetch WikiPages when the component mounts
    fetchWikiPageTitles();
  }, [location]);

  const fetchWikiPageTitles = async () => {
    try {
      const pages = await getWikiPageTitles();
      // console.log(pages);
      setWikiPageTitles(pages);
    } catch (error) {
      console.error("Error fetching WikiPages:", error);
    }
  };

  return (
    <div className="wrapAll clearfix" style={{ backgroundColor: styles.bodyColor, width: "100vw", minHeight: "100vh", fontWeight:"bold", fontFamily: styles.fontFamily}} >
      <div>
        <HeaderComponent userName={userName} userRole={userRole}/>
        {/* <Breadcrumbs/> */}
        <WikiList handleLogout={handleLogout} cookies={cookies} categories={categories}/>
        <div className="mainsection">
          {/* <div className="headerLinks"><a href={`/profile/${userName}`}>{userName}</a> {userRole}</div> */}
          <div>
            {/* Render children, which will be the specific WikiPage component */}
            <Outlet />
          </div>
          <div className="pagefooter" style={{color: styles.footerListTextColor}}>
            This is a footer | Template by <a href="http://html5-templates.com/" target="_blank" rel="nofollow" style={{color: styles.footerListLinkTextColor}}>HTML5 Templates</a>
            <div className="footerlinks" >
              <a href="#" style={{color: styles.footerListLinkTextColor}}>Privacy policy</a> 
              <a href="#" style={{color: styles.footerListLinkTextColor}}>About</a> 
              <a href="#" style={{color: styles.footerListLinkTextColor}}>Terms and conditions</a> 
              <a href="#" style={{color: styles.footerListLinkTextColor}}>Cookie statement</a> 
              <a href="#" style={{color: styles.footerListLinkTextColor}}>Developers</a>
            </div>
        </div>
        </div>
      </div>
    </div>
  );
};

export default MainPage;
