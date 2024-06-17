import React, { useState } from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';

const ReactQuillComponent = ({handleChange, content}) =>{
    // const [content, setContent] = useState('');

    // const handleChange = (value) => {
    //     setContent(value);
    // };
    const formats = [
        'header',
        'font',
        'size',
        'bold',
        'italic',
        'underline',
        'strike',
        'list',
        'bullet',
        'link',
        'image',
        'video',
        'script',
        'indent',
        'color',
        'background',
        'align',
      ];

      const modules = {
        toolbar: [
          ['bold', 'italic', 'underline', 'strike'],
          [{ 'header': 3 }],
          ['link'],
          [{ 'script': 'sub' }, { 'script': 'super' }],
          [{ 'color': [] }, { 'background': [] }]
        ],
      };

    return (
        <div>
        <ReactQuill
            theme="snow"
            value={content}
            onChange={handleChange}
            modules={modules}
            formats={formats}
            preserveWhitespace={true}
        />
        </div>
    );
};

export default ReactQuillComponent;