import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useStyleContext } from '../../Components/contexts/StyleContext';

const CategoryPageComponent = ({ pages, categories }) => {
  const { category } = useParams(); // Extract category from URL
  const [pagesByCategory, setPagesByCategory] = useState({});
  const [pagesInCurrentCategory, setPagesInCurrentCategory] = useState([])
  const {styles} = useStyleContext()
;
  useEffect(() => {
    // Organize pages by category
    const pagesByCategory = {};
    pages.forEach(page => {
      const category = categories.includes(page.category) ? page.category : 'Uncategorized'; // Check if page category exists in categories
      if (!pagesByCategory[category]) {
        pagesByCategory[category] = [];
      }
      pagesByCategory[category].push(page);
    });
    setPagesByCategory(pagesByCategory);
  }, [pages, categories]);
  

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
