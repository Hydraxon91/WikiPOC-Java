import ProfileElement from "./Components/ProfileElement";
import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from "react";
import { useUserContext } from '../../Components/contexts/UserContextProvider';
import { getUserProfileByUsername } from "../../Api/wikiUserApi";
import { useStyleContext } from "../../Components/contexts/StyleContext";
import "../../Styles/profilepage.css";

const ProfilePage = () => {
    const navigate = useNavigate();
    const {styles} = useStyleContext();
    const {decodedTokenContext} = useUserContext();
    const { username } = useParams();
    const [userProfile, setUserProfile] = useState(null);
    const [isYourProfile, setIsYourProfile] = useState(false);

    useEffect(() => { 
        if (username!==null) {
            getUserProfileByUsername(username, setUserProfile)
            .catch(error => {
                alert(`Profile for ${username} doesn't exist.`);
                navigate("/");
            });
        }
    }, [username, navigate]);

    useEffect(() => {
        const decodedTokenName = decodedTokenContext?.["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"];
        decodedTokenName && (username === decodedTokenName && setIsYourProfile(true));
    }, [decodedTokenContext]);

    return (
        <div className="profilepage article" style={{backgroundColor: styles.articleColor}}>
            {userProfile?.userName && <ProfileElement user={userProfile} canEdit={isYourProfile}/>}
        </div>
    )
}

export default ProfilePage;