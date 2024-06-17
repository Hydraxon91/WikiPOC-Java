import ProfileEditorElement from './Components/ProfileEditorElement';
import { useParams, Navigate } from 'react-router-dom';
import { useEffect, useState } from "react";
import { useUserContext } from '../../Components/contexts/UserContextProvider';
import { getUserProfileByUsername } from '../../Api/wikiUserApi';
import { useStyleContext } from '../../Components/contexts/StyleContext';

const EditProfilePage = ({cookies}) => {
    const {decodedTokenContext} = useUserContext();
    const {styles} = useStyleContext();
    const { username } = useParams();
    const [validateProfile, setValidateProfile] = useState(false);
    const [userProfile, setUserProfile] = useState(null);

    useEffect(() => {
        const decodedTokenName = decodedTokenContext?.["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"];
        // decodedTokenName && (username === decodedTokenName && setValidateProfile(true));
        if (decodedTokenName) {
            if (username === decodedTokenName) {
                setValidateProfile(true);
                getUserProfileByUsername(username, setUserProfile);
            }
        }
    }, [decodedTokenContext]);

    return (
        <div className="profilepage article" style={{backgroundColor: styles.articleColor}}>
            {validateProfile ? 
            (
                <ProfileEditorElement user={userProfile} cookies={cookies}/>
            ) : (
                <Navigate to="/" />
            )}
        </div>
    )
}

export default EditProfilePage;