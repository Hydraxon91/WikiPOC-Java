import DisplayProfileImageElement from "./DisplayProfileImageElement"
import { NavLink } from "react-router-dom";

const ProfileElement = ({user, canEdit}) => {

    return (
        <div className="profile-container">
            <div className="profilepage-profilepic">
                <DisplayProfileImageElement profilePicture={user.profilePicture} />
            </div>
             
             <div className="user-info"><p>Username: {user.userName}</p></div>
             <div className="user-info"><p>Display name: {user.displayName}</p></div>
             {canEdit && (
                <NavLink to={`/profile/edit/${user.userName}`} className="edit-profile-navlink">
                    Edit your profile
                </NavLink>
            )}
        </div>
    );
}

export default ProfileElement;