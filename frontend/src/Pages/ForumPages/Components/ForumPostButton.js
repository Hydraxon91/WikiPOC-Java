import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useStyleContext } from '../../../Components/contexts/StyleContext';
import '../Styles/modularbutton.css';

const ForumPostButton = ({ buttonTitle, linkTo, cookies }) => {
    const { styles } = useStyleContext();
    const navigate = useNavigate()

    const handleClick = () => {
        if (!cookies) { // Assuming `cookies.user` is null when the user is not logged in
            alert('You need to log in to perform this action.');
        } else {
            navigate(linkTo)
        }
    };

    return (
        <button onClick={handleClick} className="modular-button" style={{ backgroundColor: styles.articleColor }}>
            {buttonTitle}
        </button>
    );
};

export default ForumPostButton;
