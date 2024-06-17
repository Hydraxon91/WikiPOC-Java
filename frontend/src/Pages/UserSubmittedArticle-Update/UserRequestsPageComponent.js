import React, { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { getUpdatePageTitles, getNewPageTitles } from '../../Api/wikiApi';
import { useCookies } from 'react-cookie';
import { useStyleContext } from '../../Components/contexts/StyleContext';

const UserRequestsPageComponent = () => {
    const [pages, setPages] = useState([]);
    const {styles} = useStyleContext();
    const location = useLocation();
    const [cookies] = useCookies(['jwt_token']);

    useEffect(() => {
        fetchPage();
    }, [location.pathname]);
    const fetchPage = async () => {
        try {
            if (location.pathname === '/user-submissions') {
                const data = await getNewPageTitles(cookies['jwt_token']);
                setPages(data);
            }
            else if (location.pathname === '/user-updates') {
                const data = await getUpdatePageTitles(cookies['jwt_token']);
                setPages(data);
            }
        } catch (error) {
          console.error('Error fetching page:', error);
        }
      };

    return (
    <div className="article" style={{backgroundColor: styles.articleColor}}>
      {location && location.pathname === '/user-submissions' && (
        <h2 key="wiki-pages-heading">User Submitted Pages</h2>
      )}

      {location && location.pathname === '/user-updates' && (
        <h2 key="wiki-pages-heading">User Submitted Updates</h2>
      )}

      <ul key="wiki-pages-list">
        {pages &&  pages.map((pageTuple, index) => (
          <li key={index}>
            <Link to={`${location.pathname}/${encodeURIComponent(pageTuple.item2)}`}>
              <strong>{pageTuple.item1}</strong>
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default UserRequestsPageComponent;