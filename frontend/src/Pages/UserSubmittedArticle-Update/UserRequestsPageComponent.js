import React, { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { getUpdatePageTitles, getNewPageTitles } from '../../Api/wikiApi';
import { useCookies } from 'react-cookie';
import { useStyleContext } from '../../Components/contexts/StyleContext';

const UserRequestsPageComponent = () => {
    const [pages, setPages] = useState([]);
    const { styles } = useStyleContext();
    const location = useLocation();
    const [cookies] = useCookies(['jwt_token']);

    useEffect(() => {
        fetchPage();
    }, [location.pathname]);

    useEffect(() => {
        console.log('Pages state updated:', pages);
    }, [pages]);

    const fetchPage = async () => {
        try {
            let data = [];
            if (location.pathname === '/user-submissions') {
                console.log('Fetching new page titles');
                data = await getNewPageTitles(cookies['jwt_token']);
            } else if (location.pathname === '/user-updates') {
                console.log('Fetching update page titles');
                data = await getUpdatePageTitles(cookies['jwt_token']);
            }
            console.log('Fetched data:', data);
            setPages(data);
        } catch (error) {
            console.error('Error fetching page:', error);
        }
    };

    return (
        <div className="article" style={{ backgroundColor: styles.articleColor }}>
            {location.pathname === '/user-submissions' && (
                <h2>User Submitted Pages</h2>
            )}

            {location.pathname === '/user-updates' && (
                <h2>User Submitted Updates</h2>
            )}

            <ul>
                {pages && pages.map((page) => (
                    <li key={page.id}>
                        <Link to={`${location.pathname}/${encodeURIComponent(page.id)}`}>
                            <strong>{page.title}</strong>
                        </Link>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default UserRequestsPageComponent;
