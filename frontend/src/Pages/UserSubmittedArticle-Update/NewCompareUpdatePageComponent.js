import React, { useState, useEffect } from 'react';
import LegacyWikiPageComponent from '../WikiPage-Article/Components/LegacyWikiPageComponent';
import WikiPageComponent from '../WikiPage-Article/Components/WikiPageComponent';
import { useStyleContext } from '../../Components/contexts/StyleContext';
import '../../Styles/wikipage.css';

const NewCompareUpdatePage = ({page: wikipage, setDecodedTitle}) => {
    const [activeTab, setActiveTab] = useState("wiki");
    const [page, setPage] = useState(null);
    const [images, setImages] = useState(null);
    const {styles} = useStyleContext();

    useEffect(()=>{
        if (wikipage && (wikipage.wikiPage || wikipage.userSubmittedWikiPage))  {
            setActiveTab('wiki');
            setPage(wikipage.wikiPage ?? wikipage.userSubmittedWikiPage);
            setImages(wikipage.images)
        }
    },[wikipage])


    return(
        <div className="article" style={{backgroundColor: styles.articleColor}}>         
            <div className="update-page-container">
                {page && !page.legacyWikiPage ? 
                (
                    <WikiPageComponent
                        page={page}
                        setDecodedTitle={setDecodedTitle}
                        activeTab={activeTab}
                        className={activeTab === 'wiki' ? 'wikipage-visible' : 'wikipage-hidden'}
                        images={images}
                    />
                ) : (
                    <LegacyWikiPageComponent
                        page={page}
                        setDecodedTitle={setDecodedTitle}
                        activeTab={activeTab}
                        className={activeTab === 'wiki' ? 'wikipage-visible' : 'wikipage-hidden'}
                    />
                )}
            </div>
        </div>
    )
};

export default NewCompareUpdatePage;