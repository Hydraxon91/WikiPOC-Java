const BASE_URL = process.env.REACT_APP_API_URL;

export const getUserProfileByUsername = async (username, setUser) => {
    const response = await fetch(`${BASE_URL}/api/UserProfile/GetByUserName/${username}`);
    if (!response.ok) {
      throw new Error(`Failed to get UserProfile for ${username}. Status: ${response.status}`);
    }
    const data = await response.json();
    setUser(data);
  };

  export const postComment = async (comment, token) => {
    console.log(comment);
    const response = await fetch(`${BASE_URL}/api/UserComment/comment/`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(comment),
      });

      if (!response.ok) {
        // Handle the error, you can throw an exception or return an error object
        throw new Error(`Failed to delete WikiPage. Status: ${response.status}`);
      }

      const data = await response.json();
      return data;
  };

  export const postEditedComment = async (commentId, editedComment, token) => {
    const response = await fetch(`${BASE_URL}/api/UserComment/comment/${commentId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(editedComment),
      });

      if (!response.ok) {
        // Handle the error, you can throw an exception or return an error object
        throw new Error(`Failed to update comment. Status: ${response.status}`);
      }

      const data = await response.json();
      return data;
  };

export const postProfileEdit = async (profile, profilePictureFile, token) => {
  const formData = new FormData();
  formData.append('userUpdateForm.UserProfile.Id', profile.id);
  formData.append('userUpdateForm.UserProfile.DisplayName', profile.displayName);
  formData.append('userUpdateForm.UserProfile.UserName', profile.userName);
  formData.append('userUpdateForm.ProfilePictureFile', profilePictureFile);
  for (const value of formData) {
    console.log(value);
  }

  const response = await fetch(`${BASE_URL}/api/UserProfile/UpdateProfile/${profile.id}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
    body: formData,
  });

  if (!response.ok) {
    // Handle the error, you can throw an exception or return an error object
    throw new Error(`Failed to update UserProfile. Status: ${response.status}`);
  }

  const data = await response.json();
  return data;
};

export const getProfilePicture = async(pictureString) => {
  try {
    if (pictureString.startsWith('blob:')) {
      return pictureString; // Return the Blob URL directly
    }
    const response = await fetch(`${BASE_URL}/api/Image/profile/${pictureString}`);
    if (!response.ok) {
        throw new Error(`Failed to get Profile Picture ${pictureString}. Status: ${response.status}`);
    }

    // Assuming the response is the URL of the image
    const imageUrl = await response.blob();

    return imageUrl;
  } catch (error) {
      throw new Error(`Failed to fetch profile picture: ${error.message}`);
  }
};

