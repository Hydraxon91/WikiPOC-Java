import React, { useState, useEffect } from 'react';
import ReactQuill from 'react-quill';
import UserImagesContainer from './UserImagesContainer';
import '../Style/articleeditor.css';
import '../../WikiPage-Article/Style/wikipagecomponent.css'

const CustomHTMLPopup = ({ insertCustomHTML, togglePopupVisibility, images }) => {
  const [imageUrl, setImageUrl] = useState('');
  const [imageData, setImageData] = useState('');
  const [text, setText] = useState('Enter Text Here');
  const [orientation, setOrientation] = useState('left');

  const handleContentChange = (value) => {
    setText(value);
  };

  useEffect(()=>{
    if (imageUrl) {
      const image = images.find(image => image.name === imageUrl);
      if (image) {
        setImageData(image.dataURL);
      }
    }
  },[imageUrl])

  const insertImage = (imageData) =>{
    setImageUrl(imageData);
  }

  const trimText = (text) => {
    const textRegex = /<p>(.*?)<\/p>/g;
    const textMatch = Array.from(text.matchAll(textRegex)).map(match => match[1]);
    // console.log(textMatch);
    if (textMatch) {
      const trimmedText = textMatch.map(match => `<p>${match.trim()}</p>`).join('\n');
      return <div dangerouslySetInnerHTML={{ __html: trimmedText }} />
    }
  }

  const handleOrientationChange = (event) => {
    setOrientation(event.target.value);
  };
  
  const customModules = {
    toolbar: [
        ['link'],
        ['image'],
    ],
  };

  const handleImageChange = (input) => {
    if(!input) {
      setImageUrl('');
      return;
    }

    if (typeof input === 'string') {
      // If input is a string, assume it's a URL and directly set it as imageUrl
      setImageUrl(input);
    } else if (input instanceof File) {
      // If input is a File object, convert it to data URI
      fileToDataUri(input)
        .then(dataUri => {
          setImageUrl(dataUri);
        });
    }
    
  }

  const fileToDataUri = (file) => new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = (event) => {
      resolve(event.target.result);
    };
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });


  const handleConfirm = () => {
    // Create the HTML content string with placeholders
    const htmlContent = `||${orientation}//<a href="${imageUrl}" rel="noopener noreferrer" target="_blank">ImageRef</a>##<a href='${text}' rel="noopener noreferrer" target="_blank">TextRef</a>||`;
    // Insert the processed HTML content
    insertCustomHTML(htmlContent);
    togglePopupVisibility();
  };

  return (
    <div className='custom-popup-overlay'>
      <div className="custom-popup">
      
        {imageData && text &&
          <>
            <label className='article-preview'>Preview:</label>
            <div className='article-container'>
              <div className='thumbnail mid'>
                <div className='thumbnail-inner'>
                  <img className="paragraph-image" src={imageData} alt="logo"/>
                </div>
                <div className="wikipage-content-container">{trimText(text)}</div>
              </div>
            </div>
          </>
        }
        <label>Image</label>
        <UserImagesContainer images={images} insertImage={insertImage}/>

        <label>Text:</label>
        <ReactQuill 
            theme="snow" 
            value={text} 
            onChange={handleContentChange}
        />

        <label>Orientation:</label>
        <select value={orientation} onChange={handleOrientationChange}>
          <option value="left">Left</option>
          <option value="right">Right</option>
          <option value="mid">Middle</option>
          <option value='freeflow'>Freeflow</option>
        </select>

        <div className='confirm-button-container'>
          <button className='confirm-button left' onClick={handleConfirm}>Confirm</button>
          <button className='confirm-button right' onClick={togglePopupVisibility}>Cancel</button>
        </div>
      </div>
    </div>
  );
};

export default CustomHTMLPopup;