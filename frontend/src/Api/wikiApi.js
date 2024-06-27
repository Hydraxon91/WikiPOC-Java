const BASE_URL = process.env.REACT_APP_API_URL;

export const getWikiPages = async () => {
  const response = await fetch(`${BASE_URL}/api/article-pages`);
  if (!response.ok) {
    throw new Error(`Failed to get WikiPages. Status: ${response.status}`);
  }
  const data = await response.json();
  return data;
};

export const getWikiPageTitlesAndSlugs = async () => {
    const response = await fetch(`${BASE_URL}/api/article-pages/titles-and-slugs`, {
      method: 'GET',
    });
    if (!response.ok) {
      throw new Error(`Failed to get WikiPage Titles. Status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  };

export const getWikiPageById = async (id) => {
    const response = await fetch(`${BASE_URL}/api/article-pages/getbyid/${id}`);
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

  export const getWikiPageBySlug = async (slug) => {
    const response = await fetch(`${BASE_URL}/api/article-pages/getbyslug/${slug}`);
    if (!response.ok) {
      throw new Error(`Failed to get WikiPage. Status: ${response.status}`);
    }
    const data = await response.json();
    console.log(data);
    return data;
  };

  export const createWikiPage = async (newPage, token, decodedToken, images) => {
    const role = decodedToken["http://schemas.microsoft.com/ws/2008/06/identity/claims/role"];
    const userName = decodedToken["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"];
    const isAdmin = role === "ADMIN";
    const url = isAdmin ? `${BASE_URL}/api/article-pages/addAdmin` : `${BASE_URL}/api/article-pages/addUser`;

    const formData = new FormData();
    // Append fields from newPage
    formData.append('title', newPage.title);
    formData.append('categoryId', newPage.category);
    formData.append('siteSub', newPage.siteSub);
    formData.append('roleNote', newPage.roleNote);
    formData.append('content', newPage.content);

    // Additional fields for user submissions
    if (!isAdmin) {
        formData.append('isNewPage', true);
        formData.append('approved', false);
        formData.append('submittedBy', userName);

    }

    // Append images to FormData object
    console.log(images.length);
    if (images && images.length > 0) {
        images.forEach((image, index) => {
          console.log(image);
          formData.append(`images[${index}].fileName`, image.name); // Adjust based on actual structure of ImageFormModel
          formData.append(`images[${index}].dataURL`, image.dataURL); // Adjust based on actual structure of ImageFormModel
        });
    }

    try {
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
    } catch (error) {
        console.error('Error creating WikiPage:', error.message);
        throw error;
    }
};

export const updateWikiPage = async (updatedPage, token, decodedToken, images) => {
    var role = decodedToken["http://schemas.microsoft.com/ws/2008/06/identity/claims/role"];
    var userName = decodedToken["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"];
    var url = role==="ADMIN"? `${BASE_URL}/api/article-pages/update/${updatedPage.id}` : `${BASE_URL}/api/WikiPages/userUpdate/${updatedPage.id}`;
    const formData = new FormData();
    if (role !== "ADMIN") {
      formData.append('parentArticlePageId', updatedPage.id)
      formData.append('submittedBy', userName);
      formData.append('approved', false)
      formData.append('isNewPage', false);
    }
    formData.append('title', updatedPage.title);
    formData.append('categoryId', updatedPage.categoryId);
    formData.append('siteSub', updatedPage.siteSub);
    formData.append('roleNote', updatedPage.roleNote);
    formData.append('content', updatedPage.content);
    // Append images to the FormData object
    images && images.forEach((image, index) => {
      formData.append(`images[${index}].fileName`, image.name); // Adjust based on actual structure of ImageFormModel
      formData.append(`images[${index}].dataURL`, image.dataURL); // Adjust based on actual structure of ImageFormModel
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
    const response = await fetch(`${BASE_URL}/api/style`); // Replace with your actual endpoint
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
  formData.append('styleModel.logo', newStyles.logo);
  formData.append('styleModel.wikiName', newStyles.wikiName);
  formData.append('styleModel.bodyColor', newStyles.bodyColor);
  formData.append('styleModel.articleRightColor', newStyles.articleRightColor);
  formData.append('styleModel.articleRightInnerColor', newStyles.articleRightInnerColor);
  formData.append('styleModel.articleColor', newStyles.articleColor);
  formData.append('styleModel.footerListLinkTextColor', newStyles.footerListLinkTextColor);
  formData.append('styleModel.footerListTextColor', newStyles.footerListTextColor);
  formData.append('styleModel.fontFamily', newStyles.fontFamily);
  if (logoPictureFile) {
    formData.append('logoPictureFile', logoPictureFile);
  }

  try {
    const response = await fetch(`${BASE_URL}/api/style`, {
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
    const response = await fetch(`${BASE_URL}/api/public/picture/logo/${pictureString}`);
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
  const response = await fetch(`${BASE_URL}/api/article-pages/unapproved-new-page-titles`, {
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
  const response = await fetch(`${BASE_URL}/api/api/article-pages/getbyid/${id}`, {
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
  const response = await fetch(`${BASE_URL}/api/article-pages/unapproved-update-titles`, {
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
  const response = await fetch(`${BASE_URL}/api/article-pages/getbyid/${id}`, {
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

export const acceptUserSubmittedUpdate = async (id, originalId, token) => {
  try {

    console.log('Request Data:', id);
    
    const response = await fetch(`${BASE_URL}/api/article-pages/accept-update/${originalId}/${id}`, {
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
  const response = await fetch(`${BASE_URL}/api/categories`, {
    method: 'GET',
  });
  if (!response.ok) {
    throw new Error(`Failed to get Categories. Status: ${response.status}`);
  }
  const data = await response.json();
  return data;
};

export const fetchPagesForAllCategories = async (categories) => {
  console.log(categories);
  const pagesByCategory = {};
  // Fetch pages for each category
  await Promise.all(categories.map(async (category) => {
    const response = await fetch(`${BASE_URL}/api/categories/${category.slug}/approvedNotArchivedArticlePages`);
    const pages = await response.json();
    pagesByCategory[category.categoryName] = pages;
  }));
  return pagesByCategory;
}

export const fetchPagesByCategory = async (category) => {
  const pagesByCategory = {};
  // Fetch pages for each category
  const response = await fetch(`${BASE_URL}/api/categories/${category}/approvedNotArchivedArticlePages`);
  const pages = await response.json(); 
  return pages;
}

export const fetchPagesCountByCategory = async (categoryId) => {
  const response = await fetch(`${BASE_URL}/api/categories/${categoryId}/articlePages/count`);
  if (!response.ok) {
      throw new Error('Failed to fetch page count');
  }
  const count = await response.json();
  return count;
};

export const addCategory = async (categoryName, token) => {
  const response = await fetch(`${BASE_URL}/api/categories`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: categoryName
  });

  if (!response.ok) {
    throw new Error(`Failed to add category. Status: ${response.status}`);
  }

  const data = await response.json();
  // console.log(data);
  return data;
};

export const deleteCategory = async (categoryId, token) => {
  const response = await fetch(`${BASE_URL}/api/categories/${categoryId}`, {
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