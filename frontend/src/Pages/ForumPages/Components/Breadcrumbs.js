import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import "../Styles/Breadcrumbs.css";

const Breadcrumbs = () => {
    const location = useLocation();
    const pathnames = location.pathname.split('/').filter(x => x);

    const formatBreadcrumb = (breadcrumb) => {
        return breadcrumb
            .split('-')
            .map(word => word.charAt(0).toUpperCase() + word.slice(1))
            .join(' ');
    };

    return (
        <nav className="breadcrumbs">
            <ul>
                {pathnames.length > 0 ? (
                    <li>
                        <Link to="/">Home</Link>
                    </li>
                ) : (
                    <li>Home</li>
                )}
                {pathnames.map((value, index) => {
                    const to = `/${pathnames.slice(0, index + 1).join('/')}`;
                    return (
                        <React.Fragment key={to}>
                            <li>{'>>'}</li>
                            <li>
                                {index + 1 === pathnames.length ? (
                                    <span>{formatBreadcrumb(value)}</span>
                                ) : (
                                    <Link to={to}>{formatBreadcrumb(value)}</Link>
                                )}
                            </li>
                        </React.Fragment>
                    );
                })}
            </ul>
        </nav>
    );
};

export default Breadcrumbs;