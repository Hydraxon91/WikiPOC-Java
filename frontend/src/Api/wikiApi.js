const BASE_URL = process.env.REACT_APP_API_URL;

export const getWikiPages = async () => {
  const response = await fetch(`${BASE_URL}/api/WikiPages`);
  if (!response.ok) {
    throw new Error(`Failed to get WikiPages. Status: ${response.status}`);
  }
  const data = await response.json();
  return data;
};

export const getWikiPageTitles = async () => {
    const response = await fetch(`${BASE_URL}/api/WikiPages/GetTitles`, {
      method: 'GET',
    });
    if (!response.ok) {
      throw new Error(`Failed to get WikiPage Titles. Status: ${response.status}`);
    }
    const data = await response.json();
    // console.log(data);
    return data;
  };

export const getWikiPageById = async (id) => {
    const response = await fetch(`${BASE_URL}/api/WikiPages/GetById/${id}`);
    if (!response.ok) {
      throw new Error(`Failed to get WikiPage. Status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  };

  export const getWikiPageByTitle = async (title) => {
    const response = await fetch(`${BASE_URL}/api/WikiPages/GetByTitle/${title}`);
    if (!response.ok) {
      throw new Error(`Failed to get WikiPage. Status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  };

export const createWikiPage = async (newPage, token, decodedToken, images) => {
  console.log(newPage);
  var role = decodedToken["http://schemas.microsoft.com/ws/2008/06/identity/claims/role"];
  var userName = decodedToken["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"];
  var url = role==="Admin"? `${BASE_URL}/api/WikiPages/admin` : `${BASE_URL}/api/WikiPages/user`;

  const formData = new FormData();
  if (role !== "Admin") {
    formData.append('wikiPageWithImagesInputModel.IsNewPage', true)
    formData.append('wikiPageWithImagesInputModel.Approved', false)
    formData.append('wikiPageWithImagesInputModel.SubmittedBy', userName);
  }

  formData.append(`wikiPageWithImagesInputModel.Title`, newPage.title);
  formData.append(`wikiPageWithImagesInputModel.CategoryId`, newPage.category);
  formData.append(`wikiPageWithImagesInputModel.SiteSub`, newPage.siteSub);
  formData.append(`wikiPageWithImagesInputModel.RoleNote`, newPage.roleNote);
  formData.append(`wikiPageWithImagesInputModel.Content`, newPage.content);
  formData.append(`model.Paragraphs`, newPage.paragraphs);
  // formData.append(`model.Images`, images);

  // Append images to the FormData object
  images.forEach((image, index) => {
    console.log(typeof image);
    console.log(image);
    formData.append(`wikiPageWithImagesInputModel.Images[${index}].FileName`, image.name);
    formData.append(`wikiPageWithImagesInputModel.Images[${index}].DataURL`, image.dataURL);
  });
  // for (let index = 0; index < images.length; index++) {
  //   formData.append(`wikiPageWithImagesInputModel.Images`, images[index])    
  // }

  console.log(newPage);
  for (const value of formData) {
    console.log(value);
  }

  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
    body: formData,
  });

  if (!response.ok) {
    throw new Error(`Failed to create WikiPage. Status: ${response.status}`);
  }
  const data = await response.json();
  return data;
};


export const updateWikiPage = async (updatedPage, token, decodedToken, images) => {
  console.log(updatedPage);
    var role = decodedToken["http://schemas.microsoft.com/ws/2008/06/identity/claims/role"];
    var userName = decodedToken["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"];
    var url = role==="Admin"? `${BASE_URL}/api/WikiPages/admin/${updatedPage.id}` : `${BASE_URL}/api/WikiPages/userUpdate/${updatedPage.id}`;
    console.log(url);
    const formData = new FormData();
    if (role !== "Admin") {
      console.log("notadmin");
      formData.append('wikiPageWithImagesInputModel.WikiPageId', updatedPage.id)
      formData.append('wikiPageWithImagesInputModel.SubmittedBy', userName);
    }
    formData.append(`wikiPageWithImagesInputModel.Title`, updatedPage.title);
    formData.append(`wikiPageWithImagesInputModel.CategoryId`, updatedPage.category);
    formData.append(`wikiPageWithImagesInputModel.SiteSub`, updatedPage.siteSub);
    formData.append(`wikiPageWithImagesInputModel.RoleNote`, updatedPage.roleNote);
    formData.append(`wikiPageWithImagesInputModel.Content`, updatedPage.content);
    formData.append(`model.Paragraphs`, updatedPage.paragraphs);
    // Append images to the FormData object
    images && images.forEach((image, index) => {
      formData.append(`wikiPageWithImagesInputModel.Images[${index}].FileName`, image.name);
      formData.append(`wikiPageWithImagesInputModel.Images[${index}].DataURL`, image.dataURL);
    });
    // console.log(`calling ${url} ${token}`);
    const response = await fetch(url, {
      method: "PUT",
      headers: {
        // 'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: formData,
    });
    if (!response.ok) {
      throw new Error(`Failed to update WikiPage. Status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  };

export const deleteWikiPage = async (id, token) =>{
    const response = await fetch(`${BASE_URL}/api/WikiPages/admin/${id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        // Handle the error, you can throw an exception or return an error object
        throw new Error(`Failed to delete WikiPage. Status: ${response.status}`);
      }

      const data = await response.json();
      return data;
};

export const fetchCurrentStyles = async (setStyles) => {
  try {
    const response = await fetch(`${BASE_URL}/api/Style`); // Replace with your actual endpoint
    if (!response.ok) {
      throw new Error(`Failed to fetch default styles. Status: ${response.status}`);
    }
    const data = await response.json();
    setStyles(data);
  } catch (error) {
    console.error('Error fetching default styles:', error);
  }
};

export const updateStyles = async (newStyles, logoPictureFile, token) => {
  const formData = new FormData();
  formData.append('styleUpdateForm.StyleModel.WikiName', newStyles.wikiName);
  formData.append('styleUpdateForm.StyleModel.BodyColor', newStyles.bodyColor);
  formData.append('styleUpdateForm.StyleModel.ArticleRightColor', newStyles.articleRightColor);
  formData.append('styleUpdateForm.StyleModel.ArticleRightInnerColor', newStyles.articleRightInnerColor);
  formData.append('styleUpdateForm.StyleModel.ArticleColor', newStyles.articleColor);
  formData.append('styleUpdateForm.StyleModel.FooterListLinkTextColor', newStyles.footerListLinkTextColor);
  formData.append('styleUpdateForm.StyleModel.FooterListTextColor', newStyles.footerListTextColor);
  formData.append('styleUpdateForm.StyleModel.FontFamily', newStyles.fontFamily);
  formData.append('styleUpdateForm.LogoPictureFile', logoPictureFile);

  try {
    const response = await fetch(`${BASE_URL}/api/Style`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
      body: formData,
    });

    if (!response.ok) {
      throw new Error(`Failed to update styles. Status: ${response.status}`);
    }

    const updatedData = await response.json();
    // setStyles(updatedData);
  } catch (error) {
    console.error('Error updating styles:', error);
  }
};

export const getLogo = async(pictureString) => {
  try {
    if (pictureString.startsWith('blob:')) {
      return pictureString; // Return the Blob URL directly
    }
    const response = await fetch(`${BASE_URL}/api/Image/${pictureString}`);
    if (!response.ok) {
        throw new Error(`Failed to get Logo Picture ${pictureString}. Status: ${response.status}`);
    }

    // Assuming the response is the URL of the image
    const imageUrl = await response.blob();

    return imageUrl;
  } catch (error) {
      throw new Error(`Failed to fetch Logo picture: ${error.message}`);
  }
};

export const getNewPageTitles = async (token) => {
  const response = await fetch(`${BASE_URL}/api/WikiPages/GetSubmittedPageTitles`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    throw new Error(`Failed to get Submitted Article Titles. Status: ${response.status}`);
  }
  const data = await response.json();
  return data;
};


export const getNewPageById = async (id, token) => {
  const response = await fetch(`${BASE_URL}/api/WikiPages/GetSubmittedPageById/${id}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    throw new Error(`Failed to get WikiPage. Status: ${response.status}`);
  }
  const data = await response.json();
  // console.log(data);
  return data;
};

export const getUpdatePageTitles = async (token) => {
  // console.log(token);
  const response = await fetch(`${BASE_URL}/api/WikiPages/GetSubmittedUpdates`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    throw new Error(`Failed to get WikiPage Titles. Status: ${response.status}`);
  }
  const data = await response.json();
  return data;
};


export const getUpdatePageById = async (id, token) => {
  const response = await fetch(`${BASE_URL}/api/WikiPages/GetSubmittedUpdateById/${id}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
  });
  if (!response.ok) {
    throw new Error(`Failed to get WikiPage. Status: ${response.status}`);
  }
  const data = await response.json();
  // console.log(data);
  return data;
};

export const acceptUserSubmittedUpdate = async (id, token) => {
  try {

    console.log('Request Data:', id);
    
    const response = await fetch(`${BASE_URL}/api/WikiPages/AdminAccept/${id}`, {
      method: "PATCH",
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const errorData = await response.json(); // Attempt to read error details from the response body
      throw new Error(`Failed to Update WikiPage. Status: ${response.status}. Details: ${JSON.stringify(errorData)}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error in acceptUserSubmittedUpdate:', error);
    // You can choose to handle the error here or rethrow it
    throw error;
  }
};

export const acceptUserSubmittedPage = async (updatedPage, token) => {
  console.log("Acceptusersubmittedpage");
  console.log(updatedPage.userSubmittedWikiPage.id);
  const response = await fetch(`${BASE_URL}/api/WikiPages/AdminAccept`, {
    method: "POST",
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: JSON.stringify(updatedPage.userSubmittedWikiPage.id),
  });
  if (!response.ok) {
    throw new Error(`Failed to Accept WikiPage. Status: ${response.status}`);
  }
  const data = await response.json();
  return data;
};

export const declineUserSubmittedWikiPage = async (id, token) =>{
  // console.log(id);
  const response = await fetch(`${BASE_URL}/api/WikiPages/AdminDecline/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      // Handle the error, you can throw an exception or return an error object
      throw new Error(`Failed to delete WikiPage. Status: ${response.status}`);
    }

    const data = await response.json();
    return data;
};

export const fetchCategories = async () => {
  const response = await fetch(`${BASE_URL}/api/Category`, {
    method: 'GET',
  });
  if (!response.ok) {
    throw new Error(`Failed to get Categories. Status: ${response.status}`);
  }
  const data = await response.json();
  // console.log(data);
  // const categoryNames = data.map(category => ({ id: category.id, categoryName: category.categoryName }));
  // console.log(categoryNames);
  // categoryNames.push("Uncategorized");
  return data;
};


export const addCategory = async (categoryName, token) => {
  const response = await fetch(`${BASE_URL}/api/Category`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: JSON.stringify(categoryName)
  });

  if (!response.ok) {
    throw new Error(`Failed to add category. Status: ${response.status}`);
  }

  const data = await response.json();
  // console.log(data);
  return data;
};

export const deleteCategory = async (categoryId, token) => {
  const response = await fetch(`${BASE_URL}/api/Category/${categoryId}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error(`Failed to delete category. Status: ${response.status}`);
  }

  return response.status; // Returns the HTTP status code (204 for success, 404 for not found)
};