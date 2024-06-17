import React from 'react';

const ManualEditStylesComponent = ({handleChange, newStyles, handleLogoPictureChange}) => {
  const fontOptions = [
    'Arial',
    'Helvetica',
    'Times New Roman',
    'Courier New',
    'Georgia',
    'Verdana',
    'Comic Sans MS',
    'Arial Black',
    'Impact',
    'Lucida Console',
  ];

  const rgbToHex = (color) => {
    if (!color) return '#000000'; 
    // Check if color starts with #
    if (!color.startsWith('#')) {
      // Convert hexadecimal to rgb
      const [r, g, b] = color.match(/\d+/g);
      const hex = (parseInt(r) << 16) | (parseInt(g) << 8) | parseInt(b);
      return '#' + (0x1000000 + hex).toString(16).slice(1);
    } else {
      // Return rgb color as is
      return color;
    }
  };

  return (
    <div>
      <h2 className="mb-4">Admin Page</h2>
      
      <div className="form-group edit_logo">
        <p>Logo Picture: </p>
          <input
            type="file"
            accept="image/*"
            onChange={handleLogoPictureChange}
          />
      </div>

      <div className="form-group">
        <label className="mb-4">Wiki Name:</label>
        <input type="text" 
          value={newStyles.wikiName}
          style={{marginLeft: "0.5rem"}} 
          onChange={(e) => handleChange('wikiName', e.target.value)} />
      </div>

      <div className="form-group">
        <label className="mb-4">Body Color:</label>
        <input type="color" 
          className="form-control-color align-middle"
          style={{marginLeft: "0.5rem"}}  
          value={rgbToHex(newStyles.bodyColor)} 
          title="Choose your color"
          onChange={(e) => handleChange('bodyColor', e.target.value)} />
      </div>

      <div className="form-group">
        <label className="mb-4">Article Color:</label>
        <input type="color" 
          className="form-control-color align-middle"
          style={{marginLeft: "0.5rem"}}  
          value={rgbToHex(newStyles.articleColor)} 
          title="Choose your color"
          onChange={(e) => handleChange('articleColor', e.target.value)} />
      </div>

      <div className="form-group">
        <label className="mb-4">Article Right Color:</label>
        <input type="color" 
          className=" form-control-color align-middle"
          style={{marginLeft: "0.5rem"}} 
          value={rgbToHex(newStyles.articleRightColor)}
          title="Choose your color"
          onChange={(e) => handleChange('articleRightColor', e.target.value)} />
      </div>

      <div className="form-group">
        <label className="mb-4">Article Right Inner Color:</label>
        <input type="color" 
          className="form-control-color align-middle"
          style={{marginLeft: "0.5rem"}}  
          value={rgbToHex(newStyles.articleRightInnerColor)}
          title="Choose your color"
          onChange={(e) => handleChange('articleRightInnerColor', e.target.value)} />
      </div>

      <div className="form-group">
        <label className="mb-4">Sidebar and Footer Text Color:</label>
        <input type="color" 
          className="form-control-color align-middle"
          style={{marginLeft: "0.5rem"}} 
          value={rgbToHex(newStyles.footerListTextColor)}
          title="Choose your color"
          onChange={(e) => handleChange('footerListTextColor', e.target.value)} />
      </div>

      <div className="form-group">
        <label className="mb-4">Sidebar and Footer Link Color:</label>
        <input type="color" 
          className="form-control-color align-middle"
          style={{marginLeft: "0.5rem"}} 
          value={rgbToHex(newStyles.footerListLinkTextColor)} 
          title="Choose your color"
          onChange={(e) => handleChange('footerListLinkTextColor', e.target.value)} />
      </div>

      <div className="form-group font-change">
        <label className="mb-4">Font Family:</label>
        <select
          value={newStyles.fontFamily}
          onChange={(e) => handleChange('fontFamily', e.target.value)}
        >
          {fontOptions.map((font, index) => (
            <option key={index} value={font}>{font}</option>
          ))}
        </select>
      </div>

    </div>
  );
};

export default ManualEditStylesComponent;
