import React, { useEffect, useState, useRef } from 'react';
import { Link, useParams} from 'react-router-dom';
import '../../../Styles/style.css';
import { useStyleContext } from '../../../Components/contexts/StyleContext';

const LegacyWikiPageComponent = ({page, setDecodedTitle, activeTab}) => {
  const { styles }  = useStyleContext();
  const { title } = useParams();
  const decodedTitle = decodeURIComponent(title);
  const targetRefs = useRef([]);
  const [pTitles, setPTitles] = useState([]);
  const [isContentsVisible, setIsContentsVisible] = useState(true);

  useEffect(() => {
    if (page && page.paragraphs) {
      const paragraphTitles = page.paragraphs.map((paragraph) => paragraph.title).slice(1);
      setPTitles(paragraphTitles);
    }
  }, [page]);

  useEffect(() => {
    if (setDecodedTitle) {
      setDecodedTitle(decodedTitle);
    }
  }, [decodedTitle]);

  const toggleContentsVisibility = () => {
    setIsContentsVisible(!isContentsVisible);
  };

  const scrollToParagraph = (index) => {
    if (targetRefs.current[index]) {
      targetRefs.current[index].scrollIntoView({ behavior: 'smooth' });
    }
  };


  const renderParagraphs = (content, hasParagraphImage) => {
    const additionalLines = hasParagraphImage ? Math.max(9 - content.split('<p>').length, 0) : 0;
    const maxLengthToAddLines = 100;

    return (
      <>
        <div className={"wikipage-content-container"} dangerouslySetInnerHTML={{ __html: content }} />
        {content.length <= maxLengthToAddLines && // Add lines only if content length meets the requirement
        Array.from({ length: additionalLines }).map((_, i) => (
          <br key={`empty-line-${i}`} />
        ))}
      </>
    );
  };
  
  const renderEditButton = () => {
    const pathName = window.location.pathname;
    if (pathName.includes('/page/')) {
      return (
        <Link to={`/page/${page.title}/edit`}>
          <img className="editButton" src="/img/edit.png" alt="Edit" />
        </Link>
      );
    }
    return null;
  };

  return (
    <>
      {page && (
        <div className={activeTab === 'wiki' ? 'wikipage-component' : 'wikipage-component wikipage-hidden'}>
          <h1>
            {page.title}
            {renderEditButton()}
          </h1>

          {page.siteSub && (<p className="siteSub">{`${page.siteSub}`}</p>)}
          {page.roleNote && <p className="roleNote">{`${page.roleNote}`}</p>}

          {page.paragraphs && page.paragraphs.map((paragraph, index) => (
            <div key={`paragraph-${index}`} ref={(el) => (targetRefs.current[index] = el)} className={page.approved === false ? 'update-paragraph' : ''}>
              {index!==0 && <h2>{paragraph.title}</h2>}
              {paragraph.paragraphImage && paragraph.paragraphImage !== "" && (
                <div className="articleRight" style={{ backgroundColor: styles.articleRightColor }}>
                  <div className="articleRightInner" style={{ backgroundColor: styles.articleRightInnerColor }}>
                    <img className='paragraphImage' src={paragraph.paragraphImage} alt="logo" />
                  </div>
                  {renderParagraphs(paragraph.paragraphImageText)}
                  
                </div>
              )}

              {renderParagraphs(paragraph.content, Boolean(paragraph.paragraphImage))}

              {index === 0 && pTitles.length > 0 && 
                (
                  <div 
                    className={`contentsPanel ${isContentsVisible ? '' : 'minimizedPanel'}`}
                    style={{backgroundColor: styles.articleRightColor}} 
                  >
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
          ))}
        </div>
      )}
    </>
  );
};

export default LegacyWikiPageComponent;
