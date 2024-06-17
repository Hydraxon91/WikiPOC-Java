import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useStyleContext } from '../../Components/contexts/StyleContext';
import EditPageComponent from './Components/LegacyEditPageComponent';
import LegacyWikiPageComponent from '../WikiPage-Article/Components/LegacyWikiPageComponent';


const LegacyEditPage = ({ page, handleFieldChange, handleSave, category, setCategory }) => {
  const {styles} = useStyleContext();
  const [temporaryPage, setTemporaryPage] = useState(null);
  const [title, setTitle] = useState('');
  const [siteSub, setSiteSub] = useState('');
  const [roleNote, setRoleNote] = useState('');
  const [newPage, setNewPage] = useState(true);
  const [paragraphs, setParagraphs] = useState([]);
  const [emptyFields, setEmptyFields] = useState([]);

  useEffect(() => {
    console.log(page);
    if (page) {
      setTemporaryPage(page);
      setTitle(page.title);
      setRoleNote(page.roleNote);
      setSiteSub(page.siteSub);
      setParagraphs([...page.paragraphs]);
      setNewPage(false);
    }
    else{
      setTemporaryPage(null);
      setTitle("");
      setRoleNote("");
      setSiteSub("");
      setParagraphs([]);
      setNewPage(true);
    }
  }, [page]);


  const handleAddParagraph = () => {
    const updatedParagraphs = [
      ...paragraphs,
      {
        title: '',
        content: '',
        siteSub: '',
        roleNote: '',
        paragraphImage: '',
        paragraphImageText: ''
      }
    ];
    handleFieldChange('paragraphs', updatedParagraphs);
    // updateTemporaryPage(title, siteSub, roleNote, updatedParagraphs);
  };

  const handleRemoveParagraph = (index) => {
    const updatedParagraphs = [...paragraphs];
    updatedParagraphs.splice(index, 1);
    handleFieldChange('paragraphs', updatedParagraphs);
    // updateTemporaryPage(title, siteSub, roleNote, updatedParagraphs);
  };

  const handleParagraphChange = (index, field, value) => {
    const updatedParagraphs = [...paragraphs];
    updatedParagraphs[index][field] = value;
    handleFieldChange('paragraphs', updatedParagraphs);
    // updateTemporaryPage(title, siteSub, roleNote, updatedParagraphs);
  };

  
  const updateTemporaryPage = (field, value) => {
    setTemporaryPage(prevState => ({
      ...prevState,
      [field]: value,
    }));
  };



  return (
    <div style={{backgroundColor: styles.articleColor, display: 'flex'}} className="article">
      <EditPageComponent newPage={newPage} title={title} handleFieldChange={handleFieldChange} siteSub={siteSub} roleNote={roleNote} paragraphs={paragraphs} emptyFields={emptyFields} handleParagraphChange={handleParagraphChange} handleRemoveParagraph={handleRemoveParagraph} handleAddParagraph={handleAddParagraph} handleSave={handleSave} category={category} />
      <LegacyWikiPageComponent page={temporaryPage} activeTab={"wiki"}/>
    </div>
  );
};

export default LegacyEditPage;
