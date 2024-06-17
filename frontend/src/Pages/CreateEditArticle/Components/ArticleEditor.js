import React, { useRef, useEffect, useState } from 'react';
import ReactQuill from 'react-quill';
import CustomHTMLPopup from './CustomHTMLPopup';
import UserImagesContainer from './UserImagesContainer';
import { fetchCategories } from '../../../Api/wikiApi';
import { useStyleContext } from '../../../Components/contexts/StyleContext';
import 'react-quill/dist/quill.snow.css';


const ArticleEditor = ({ title, siteSub, roleNote, content, handleFieldChange, handleContentChange, handleSave, images, setImages, category }) => {
  const quillRef = useRef(null); // Define quillRef
  const {styles} = useStyleContext();
  const [lastSelection, setLastSelection] = useState(null);
  const [isPopupVisible, setIsPopupVisible] = useState(false);
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    if (quillRef.current) {
      // Initialize Quill editor
      const editor = quillRef.current.getEditor();
      if (editor) {
        editor.enable(true);
        // editor.getModule('toolbar').addHandler('custom-html', insertCustomHTML);
      }
    }
    getCategories();
  }, []);

  useEffect(() => {
    const editor = quillRef.current?.getEditor();
    if (editor) {
      let timeoutId;

      const handleChange = (delta, oldDelta, source) => {
        if (source === 'user') {
          clearTimeout(timeoutId); // Clear previous timeout
          timeoutId = setTimeout(() => {
            const selection = editor.getSelection();
            if (selection !== null) {
              // console.log(selection);
              setLastSelection(selection);
            }
          }, 100); // Adjust the delay as needed
        }
      };

      editor.on('selection-change', handleChange);
      editor.on('text-change', handleChange);

      return () => {
        editor.off('selection-change', handleChange);
        editor.off('text-change', handleChange);
      };
    }
  }, [quillRef]);

  const togglePopupVisibility = () => {
    setIsPopupVisible(!isPopupVisible);
  };

  const customModules = {
    toolbar: [
        ['bold', 'italic', 'underline', 'strike'],
        [{ 'header': [2, 3, false] }],
        ['link'],
        [{ 'image': {} }],
    ],
  };

  const insertCustomHTML = (htmlContent) => {
    const editor = quillRef.current?.getEditor();
    if (editor) {
      const cursorPosition = lastSelection ? lastSelection.index + lastSelection.length : editor.getSelection();
      editor.clipboard.dangerouslyPasteHTML(cursorPosition, htmlContent, 'user');
    } else {
      console.error('Could not get current selection.');
    }
  };

  const insertImageToEditor = (imageData) =>{
    const editor = quillRef.current?.getEditor();
    if (editor) {
      const cursorPosition = lastSelection ? lastSelection.index + lastSelection.length : editor.getSelection();
      const insertData = `<img src="${imageData}" alt="${imageData}"/>`;
      editor.clipboard.dangerouslyPasteHTML(cursorPosition, insertData, 'user');
    } else {
      console.error('Could not get current selection.');
    }
  }

  const handleImageInsertFromDevice = (event) => {
    const files = event.target.files;

    const acceptedTypes = ['image/jpeg', 'image/png', 'image/gif']; // Accepted image types
    const maxAspectRatio = 2; // Maximum aspect ratio (width / height)
    const maxSizeInBytes = 10 * 1024 * 1024; // Maximum size in bytes (10MB)
    const newImages = [];

    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        let fileName = file.name;

        // Check if the file name already exists in the images array
        let count = 2;
        while (images.some(image => image.name === fileName)) {
            // If the name already exists, append '(2)' (or next available number) to the file name
            fileName = `${file.name.substring(0, file.name.lastIndexOf('.'))} (${count})${file.name.substring(file.name.lastIndexOf('.'))}`;
            count++;
        }

        // Check if the file type is accepted
        if (acceptedTypes.includes(file.type)) {
            // Check if the file size is within the limit
            if (file.size <= maxSizeInBytes) {
                const reader = new FileReader();

                reader.onload = (e) => {
                    const img = new Image();
                    img.src = e.target.result;

                    img.onload = () => {
                        const aspectRatio = img.width / img.height;

                        // Check if the aspect ratio is within the limit
                        if (aspectRatio <= maxAspectRatio) {
                            // Add the image to the list of images with the modified name
                            newImages.push({ name: fileName, dataURL: e.target.result });
                            setImages([...images, { name: fileName, dataURL: e.target.result }]);
                        } else {
                            // Display an error message or handle the invalid aspect ratio
                            window.alert(`Image ${fileName} has an invalid aspect ratio.`);
                        }
                    };
                };

                reader.readAsDataURL(file);
            } else {
                // Display an error message or handle oversized images
                window.alert(`Image ${fileName} exceeds the maximum size limit of 10 megabytes.`);
            }
        } else {
            // Display an error message or handle unsupported file types
            window.alert(`File type not supported: ${file.type}`);
        }
    }
};

const getCategories = async () => {
  try {
    const fetchedCategories = await fetchCategories();
    console.log(fetchedCategories);
    // const categoryNames = fetchedCategories.map(category => (category.categoryName));
    setCategories(fetchedCategories);
  } catch (error) {
    console.error("Error fetching categories:", error);
    // Handle the error if necessary
  }
}


  return (
    <div className="article article-editor" style={{backgroundColor: styles.articleColor}}>
      <div className='editDiv'>
        <label className="editLabel">Article Title:</label>
        <input 
          type="text" 
          value={title} 
          onChange={(e) => handleFieldChange('title', e.target.value)} 
        />
      </div>
      <div className='editDiv'>
        <label className="editLabel">Category:</label>
        <select 
          value={category}
          onChange={(e) => handleFieldChange('category', e.target.value)}
        >
          <option value="">Select Category</option>
          {categories.length > 0 && categories.map((cat, index) => (
            <option key={index} value={cat.id}>{cat.categoryName}</option>
          ))}
        </select>
      </div>
      <div className='editDiv'>
        <label className="editLabel">Site Sub [Optional]</label>
        <input 
          type="text" 
          value={siteSub} 
          onChange={(e) => handleFieldChange('siteSub', e.target.value)} 
        />
      </div>
      <div className='editDiv'>
        <label className="editLabel">Role Note [Optional]</label>
        <input 
          type="text" 
          value={roleNote} 
          onChange={(e) => handleFieldChange('roleNote', e.target.value)} 
        />
      </div>
      <div>
        {/* <CustomQuillToolbar togglePopupVisibility={togglePopupVisibility} /> */}
        <ReactQuill
            value={content? content : ''} 
            onChange={handleContentChange}
            modules={customModules}
            ref={quillRef}
        />
        <button onClick={togglePopupVisibility}>Insert Thumb</button>
        <input type="file" id='file' accept="image/*" className='custom-file-upload' onChange={handleImageInsertFromDevice} multiple />
        <label htmlFor="file" className='label-for-custom-file-upload'>Choose files</label>
        {/* Render the popup component if isPopupVisible is true */}
        {isPopupVisible && <CustomHTMLPopup insertCustomHTML={insertCustomHTML} togglePopupVisibility={togglePopupVisibility} images={images}/>}
        <UserImagesContainer images={images} insertImage={insertImageToEditor}/>
      </div>
      <button onClick={() => handleSave(content)}>Save</button>
    </div>
  );
}

export default ArticleEditor;
