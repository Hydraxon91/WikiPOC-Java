import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useStyleContext } from '../../../Components/contexts/StyleContext';
import { fetchPagesForAllCategories } from '../../../Api/wikiApi';

const HomeComponent = ({ pages, categories }) => {
  const [pagesByCategory, setPagesByCategory] = useState({});
  const { styles } = useStyleContext();

  useEffect(() => {
    const loadPagesByCategory = async () => {
      if (categories) {
        const pagesByCategory = await fetchPagesForAllCategories(categories);
        setPagesByCategory(pagesByCategory);
      }
    };
    loadPagesByCategory();
  }, [categories]);

  return (
    <div className='home-component article' style={{ backgroundColor: styles.articleColor }}>
      <h2>Wiki Articles Categorized</h2>
      {Object.entries(pagesByCategory).map(([category, pages]) => (
        <div key={category}>
          <h3>{category}</h3>
          <ul>
            {pages.map((page, index) => (
              <li key={index}>
                <Link to={`/page/${encodeURIComponent(page.slug)}`}>
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
