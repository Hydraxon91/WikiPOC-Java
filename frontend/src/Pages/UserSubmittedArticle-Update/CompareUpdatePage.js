import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import { getUpdatePageById, getWikiPageById, acceptUserSubmittedUpdate, declineUserSubmittedWikiPage } from '../../Api/wikiApi';
import CompareUpdatePageComponent from './CompareUpdatePageComponent';
import NewCompareUpdatePage from './NewCompareUpdatePageComponent';
import './Style/compareupdates.css';

const CompareUpdatePage = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const [cookies] = useCookies(['jwt_token']);
    const [originalPage, setOriginalPage] = useState();
    const [updatePage, setUpdatePage] = useState();

    useEffect(() => {
        // console.log(location.pathname);
        const match = location.pathname.match(/\/([a-f\d-]+)$/i);
        // console.log(match);
        const numberAtEnd = match ? match[1] : null;
        fetchUpdatePage(numberAtEnd);
    }, [location.pathname]);

    useEffect(()=>{
        // console.log(updatePage);
        if (updatePage && updatePage.userSubmittedWikiPage.wikiPageId) {
            fetchOriginalPage(updatePage.userSubmittedWikiPage.wikiPageId);
        }
    },[updatePage])

    const fetchUpdatePage = async (id) => {
        try {
            const data = await getUpdatePageById(id, cookies['jwt_token'])
            console.log(data);
            setUpdatePage(data)
        } catch (error) {
          console.error('Error fetching page:', error);
        }
      };

      const fetchOriginalPage = async (title) => {
        try {
            const data = await getWikiPageById(title)
            // console.log(data);
            setOriginalPage(data)
        } catch (error) {
          console.error('Error fetching page:', error);
        }
      };

      const handleAccept = () => {
        // console.log(updatePage);
        // console.log(originalPage);
        acceptUserSubmittedUpdate(updatePage.userSubmittedWikiPage.id, cookies["jwt_token"])
          .then(() => {
            // setWikiPageTitles(wikiPageTitles.filter((page) => page !== currentWikiPage.Title));
            alert("Succesfully updated page");
            navigate(`/user-updates`);
          })
          .catch((error) => {
            console.error("Error updating WikiPage:", error);
          });
      };

      const handleDecline = () => {
        // console.log(updatePage.userSubmittedWikiPage.id);
        declineUserSubmittedWikiPage(updatePage.userSubmittedWikiPage.id, cookies["jwt_token"])
          .then(() => {
            // setWikiPageTitles(wikiPageTitles.filter((page) => page !== currentWikiPage.Title));
            alert("Declined Change");
            navigate(`/user-updates`);
          })
          .catch((error) => {
            console.error("Error deleting WikiPage:", error);
          });
      };

      return (
        <>
            <div className='compareUpdatePageButtonsDiv'>
                <button className='compareUpdatePageButtons' onClick={() => handleAccept()}>Accept Change</button>
                <button className='compareUpdatePageButtons' onClick={() => handleDecline()}>Discard Change</button>
            </div>
            <div className='compare-updates-container'>
            {
                  <>
                    <NewCompareUpdatePage page={originalPage} disableNavbar={true}></NewCompareUpdatePage>
                    <div style={{ borderRight: '1px solid #ccc', margin: '0 10px' }}></div>
                    <NewCompareUpdatePage page={updatePage} disableNavbar={true}></NewCompareUpdatePage>
                  </> 
            }
            </div>
        </>
      )
}

export default CompareUpdatePage;