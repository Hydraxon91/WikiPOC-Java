import React from 'react';

const PresetCardComponent = ({stylePreset, logo, handleChange}) =>{

    const handlePresetClick = () => {
        const { bodyColor, articleColor, articleRightColor, articleRightInnerColor, footerListTextColor, footerListLinkTextColor, fontFamily } = stylePreset;
        handleChange('bodyColor', bodyColor);
        handleChange('articleColor', articleColor);
        handleChange('articleRightColor', articleRightColor);
        handleChange('articleRightInnerColor', articleRightInnerColor);
        handleChange('footerListTextColor', footerListTextColor);
        handleChange('footerListLinkTextColor', footerListLinkTextColor);
        handleChange('fontFamily', fontFamily);
    };

    return(
        <div className="wikipage-preset-card-component" style={{ backgroundColor: stylePreset.bodyColor, fontFamily: stylePreset.fontFamily}} onClick={handlePresetClick}>
            <div className="article-right-preset" style={{ backgroundColor: stylePreset.articleRightColor }}>
                  <div className="article-right-inner-preset" style={{ backgroundColor: stylePreset.articleRightInnerColor }}>
                    <img className='paragraphImage' src={"/img/logo.png"} alt="logo" />
                  </div>
                  {"This is a text"}      
            </div>
            <div className={"wikipage-content-container"}>Content of the 1st Paragraph</div>
            <h2>Paragraph 2 Title</h2>
            <div className={"wikipage-content-container"}>Content of the 2nd Paragraph</div>
            <h2>Paragraph 3 Title</h2>
            <div className={"wikipage-content-container"}>Content of the 3rd Paragraph</div>
            <div className="pagefooter-template" style={{ color: stylePreset.footerListTextColor }}>
                This is a footer
                <div className="footerlinks"> 
                    <a href="#" style={{ color: stylePreset.footerListLinkTextColor }} >This is a footer link</a>
                </div>
            </div>
        </div>
    )
}

export default PresetCardComponent;