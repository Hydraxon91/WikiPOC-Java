const BASE_URL = process.env.REACT_APP_API_URL;

export const getUserProfileByUsername = async (username, setUser) => {
    const response = await fetch(`${BASE_URL}/api/user-profiles/username/${username}`);
    if (!response.ok) {
      throw new Error(`Failed to get UserProfile for ${username}. Status: ${response.status}`);
    }
    const data = await response.json();
    setUser(data);
  };

  export const postComment = async (comment, token) => {
    const response = await fetch(`${BASE_URL}/api/article-pages/${comment.wikiPageId}/comment`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(comment),
      });
      if (!response.ok) {
        // Handle the error, you can throw an exception or return an error object
        throw new Error(`Failed to post comment. Status: ${response.status}`);
      }

      const contentType = response.headers.get('Content-Type');
      let responseData;

      if (contentType && contentType.includes('application/json')) {
          responseData = await response.json(); // Parse JSON response
      } else {
          responseData = await response.text(); // Read plain text response
      }

      console.log('Response:', responseData); // Log the response data

      return responseData;
  };

  export const postEditedComment = async (commentId, editedComment, token) => {
    const response = await fetch(`${BASE_URL}/api/user-comments/comment/${commentId}`, {
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
    console.log(token);
    formData.append('profile', new Blob([JSON.stringify({
      id: profile.id,
      displayName: profile.displayName,
      userName: profile.userName,
    })], { type: 'application/json' }));
    if (profilePictureFile) {
      formData.append('profilePicture', profilePictureFile);
    }

    const response = await fetch(`${BASE_URL}/api/user-profiles/update`, {
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

export const getProfilePicture = async(username) => {
  try {
    const response = await fetch(`${BASE_URL}/api/public/picture/profile/${username}`);
    if (!response.ok) {
        throw new Error(`Failed to get Profile Picture for user: ${username}. Status: ${response.status}`);
    }

    // Assuming the response is the URL of the image
    const imageUrl = await response.blob();

    return imageUrl;
  } catch (error) {
      throw new Error(`Failed to fetch profile picture: ${error.message}`);
  }
};

