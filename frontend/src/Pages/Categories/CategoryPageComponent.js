import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useStyleContext } from '../../Components/contexts/StyleContext';
import { fetchPagesByCategory } from '../../Api/wikiApi';

const CategoryPageComponent = ({ pages, categories }) => {
  const { category } = useParams(); // Extract category from URL
  const [pagesByCategory, setPagesByCategory] = useState({});
  const [pagesInCurrentCategory, setPagesInCurrentCategory] = useState([])
  const {styles} = useStyleContext()
;
useEffect(() => {
    const fetchPages = async () => {
      const pages = await fetchPagesByCategory(category);
    setPagesInCurrentCategory(pages);
  };

  fetchPages();
}, [category]);
  

  useEffect(()=>{
    setPagesInCurrentCategory(pagesByCategory[category] || [])
  },[pagesByCategory])

  return (
    <div className='home-component article' style={{backgroundColor: styles.articleColor}}>
      <h2>Category: {category}</h2>
      {pagesInCurrentCategory.length > 0 ? (
        <ul>
          {pagesInCurrentCategory.map((page, index) => (
            <li key={index}>
              <Link to={`/page/${encodeURIComponent(page.title)}`}>
                {page.title}
              </Link>
            </li>
          ))}
        </ul>
      ) : (
        <p>There are no pages in this category yet.</p>
      )}
    </div>
  );
};

export default CategoryPageComponent;
