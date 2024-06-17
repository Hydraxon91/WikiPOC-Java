import React, {useState, useEffect} from 'react';
import { getProfilePicture } from '../../../Api/wikiUserApi';
import "../../../Styles/profilepage.css";

function DisplayProfileImageElement({profilePicture, classNameProp}) {
    const defaultImageUrl = 'https://upload.wikimedia.org/wikipedia/commons/1/1e/Default-avatar.jpg'; 
    const [imageSrc, setImageSrc] = useState(defaultImageUrl);

    useEffect(()=>{
        if (profilePicture) {
            // Fetch profile picture when the component mounts or profilePicture prop changes
            getProfilePicture(profilePicture)
                .then(data => {
                    if (data instanceof Blob) { // Check if data is a Blob object
                        const imageUrl = URL.createObjectURL(data);
                        setImageSrc(imageUrl);
                    } else if (typeof data === 'string' && data.startsWith('blob:')) {
                        setImageSrc(data);
                    } else {
                        console.error('Invalid profile picture data:', data);
                        throw new Error('Invalid profile picture data');
                    }
                })
                .catch(error => {
                    console.error('Error fetching profile picture:', error);
                    // Use default image URL in case of error
                    setImageSrc(defaultImageUrl);
                });
        }
    },[profilePicture])


    return (
        <div className={classNameProp}>
            <img src={imageSrc} alt="Uploaded"/>
        </div>
    );
}

export default DisplayProfileImageElement;