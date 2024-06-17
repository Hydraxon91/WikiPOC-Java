import React, { useEffect, useState } from "react";
import { Link } from 'react-router-dom';
import { useUserContext } from '../../../Components/contexts/UserContextProvider';
import { getNewPageTitles, getUpdatePageTitles } from "../../../Api/wikiApi";

const WikiList = ({ handleLogout, cookies, categories}) => {
  const {decodedTokenContext, updateUser} = useUserContext();
  const [role, setRole] = useState(null);
  const [pagesWaitingForApproval, setPagesWaitingForApproval] = useState();
  const [updatesWaitingForApproval, setUpdatesWaitingForApproval] = useState();


  useEffect(() => {
    if (decodedTokenContext) {
      var role = decodedTokenContext["http://schemas.microsoft.com/ws/2008/06/identity/claims/role"];
      setRole(role);
      if (role === "Admin") {
        fetchNewPageTitles(cookies["jwt_token"]);
        fetchUpdatePageTitles(cookies["jwt_token"]);
      }
    }
  }, [decodedTokenContext]);


  const fetchNewPageTitles = async (token) => {
    try {
      const pages = await getNewPageTitles(token);
      setPagesWaitingForApproval(pages.length);
    } catch (error) {
      console.error("Error fetching WikiPages:", error);
    }
  };
  const fetchUpdatePageTitles = async (token) => {
    try {
      const updates = await getUpdatePageTitles(token);
      setUpdatesWaitingForApproval(updates.length);
    } catch (error) {
      console.error("Error fetching WikiPages:", error);
    }
  };

  const UserTools = () =>{
    return role==="Admin" ?
     (
      <>
        <h3 style={{marginBottom:"5px", fontSize:'110%'}}>Admin Tools</h3>
          <ul>
              <li>
                <Link key="approve-new-page-link" to="/user-submissions">
                  Pages Awaiting for Approval: {pagesWaitingForApproval}
                </Link>
              </li>
              <li>
                <Link key="approve-page-update-link" to="/user-updates">
                  Updates Awaiting for Approval: {updatesWaitingForApproval}
                </Link>
              </li>
              <li>
                <Link key="create-new-page-link" to="/create">
                  Create New Page
                </Link>
              </li>
              <li>
                <Link key="edit-wiki-link" to="/edit-wiki">
                  Edit Wiki
                </Link>
              </li>
              <li>
                <Link key="edit-categories" to="/categories/edit">
                  Edit Categories
                </Link>
              </li>
              <li>
                <button onClick={() => handleLogout(updateUser)} className="logout-button">
                  Logout
                </button>
              </li>
        </ul>
      </>
    ) :
    (
      <>
        <h3 style={{marginBottom:"5px", fontSize:'110%'}}>User Tools</h3>
          <ul>
              <li>
                <Link key="create-new-page-link" to="/create">
                  Create New Page
                </Link>
              </li>
              <li>
                <button onClick={() => handleLogout(updateUser)} className="logout-button">
                  Logout
                </button>
              </li>
        </ul>
      </>
    )
  }

  const LoginTools = () => {
    return(
      <>
      <h3 style={{marginBottom:"5px", fontSize:'110%'}}>Login Tools</h3>
          <ul>
              <li>
                <Link key="login-page-link" to="/login">
                  Login
                </Link>
              </li>
              <li>
                <Link key="register-page-link" to="/register">
                  Register
                </Link>
              </li>
        </ul>
      </>
    )
  }
  
return (
  <div className="sidebar">
    <div className="navigation">
      <h3 style={{marginBottom:"5px", fontSize:'110%'}}>Categories</h3>
      {categories && categories.map((category, index) => (
        <div key={index}>
          <Link to={`/categories/${encodeURIComponent(category)}`}>
            <p style={{marginBottom:'4px', fontSize:'80%'}}>{category}</p>
          </Link>
        </div>
      ))}
      <h3 style={{marginBottom:"5px", fontSize:'110%'}}>Forum Tools</h3>
      <ul>
        <li>
          <Link key="forum-page-link" to="/forum">
            Forum
          </Link>
        </li>
      </ul>
      {decodedTokenContext ? UserTools() : LoginTools()}
    </div>
    
  </div>
);
};

export default WikiList;
