import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { StyleProvider, useStyleContext } from '../../../Components/contexts/StyleContext';

const HomeComponent = ({ pages, categories }) => {
  const [pagesByCategory, setPagesByCategory] = useState({});
  const {styles} = useStyleContext();

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

  return (
    <div className='home-component article' style={{backgroundColor: styles.articleColor}}>
      <h2>Wiki Articles Categorized</h2>
      {Object.entries(pagesByCategory).map(([category, pages]) => (
        <div key={category}>
          <h3>{category}</h3>
          <ul>
            {pages.map((page, index) => (
              <li key={index}>
                <Link to={`/page/${encodeURIComponent(page.title)}`}>
                  {page.title}
                </Link>
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
};

export default HomeComponent;
