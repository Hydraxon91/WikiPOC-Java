import React, { useEffect, useState, useRef } from 'react';
import { Link, useLocation} from 'react-router-dom';
import '../../Styles/style.css';
import { useStyleContext } from '../../Components/contexts/StyleContext';

const CompareUpdatePageComponent = ({page, originalPage}) => {
  const { styles }  = useStyleContext();
  const location = useLocation();
  const targetRef = useRef(null);
  const [pTitles, setPTitles] = useState([]);
  const [isContentsVisible, setIsContentsVisible] = useState(true);

  useEffect(() => {
    if (page) {
      const paragraphTitles = page.paragraphs.map((paragraph) => paragraph.title).slice(1);
      setPTitles(paragraphTitles);
    }
  }, [page]);

  const toggleContentsVisibility = () => {
    setIsContentsVisible(!isContentsVisible);
  };

  const scrollToParagraph = () => {
    if (targetRef.current) {
      targetRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  };

  const renderParagraphs = (content, hasParagraphImage) => {
    const additionalLines = hasParagraphImage ? Math.max(9 - content.split('<p>').length, 0) : 0;
  
    return (
      <>
        <div dangerouslySetInnerHTML={{ __html: content }} />
        {Array.from({ length: additionalLines }).map((_, i) => (
          <br key={`empty-line-${i}`} />
        ))}
      </>
    );
  };
  
  const areParagraphsEqual = (paragraph1, paragraph2) => {
    // Compare relevant properties, adjust as needed
    return (
      paragraph1.title === paragraph2.title &&
      paragraph1.content === paragraph2.content &&
      paragraph1.paragraphImage === paragraph2.paragraphImage &&
      paragraph1.paragraphImageText === paragraph2.paragraphImageText
    );
  };

  return (
    <>
      {page && (
        <div className='comparePageComponent'>
          {
            location.pathname.match(/^\/user-updates\/(\d+)$/) && (
              <>
              {page.approved === false ? (
                <h1>User Submitted Update</h1>
              ) : (
                <h1>Original Page</h1>
              )}
            </>
            )
          }
          <h1>
            {page.title}
          </h1>

          <p className={originalPage && originalPage.sitesub!==page.siteSub ? 'siteSub updateParagraph' : 'siteSub'}>{`${page.siteSub}`}</p>
          <p className={originalPage && originalPage.roleNote!==page.roleNote ? 'roleNote updateParagraph' : 'roleNote'}>{`${page.roleNote}`}</p>

          {page.paragraphs.map((paragraph, index) => {
            const originalParagraph = originalPage && originalPage.paragraphs[index];
            const isNewParagraph = originalPage && !originalParagraph;

            return (
                <div key={`paragraph-${index}`} className={isNewParagraph || (originalParagraph && !areParagraphsEqual(paragraph, originalParagraph)) ? 'updateParagraph' : ''}>
                {index!==0 && <h2>{paragraph.title}</h2>}
                {paragraph.paragraphImage && paragraph.paragraphImage !== "" && (
                    <div className="articleRight" style={{ backgroundColor: styles.articleRightColor }}>
                    <div className="articleRightInner" style={{ backgroundColor: styles.articleRightInnerColor }}>
                        <img className='paragraphImage' src={paragraph.paragraphImage} alt="logo" />
                    </div>
                    {paragraph.paragraphImageText}
                    
                    </div>
                )}

                {renderParagraphs(paragraph.content, Boolean(paragraph.paragraphImage))}

                {index === 0 && pTitles.length > 0 && 
                    (
                    <div className={`contentsPanel ${isContentsVisible ? '' : 'minimizedPanel'}`}>
                        <div className="hidePanel"  onClick={toggleContentsVisibility}>[hide]</div>
                        <div className="showPanel"  onClick={toggleContentsVisibility}>[show]</div>
                        <div className="contentsHeader">Contents</div>
                        <ul style={{ display: isContentsVisible ? 'block' : 'none' }}>
                        {pTitles.map((paragraphTitle, titleIndex) => (
                        <li key={`title-${titleIndex}`}>
                            <span>{`${titleIndex + 1}`}</span>
                            <Link to="#" onClick={() => scrollToParagraph(titleIndex)}>
                            {paragraphTitle}
                            </Link>
                        </li>
                        ))}
                        </ul>
                    </div>
                    )
                }

                </div>
          )})}
        </div>
      )}
    </>
  );
};

export default CompareUpdatePageComponent;
