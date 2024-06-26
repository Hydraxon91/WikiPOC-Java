import React, { useState, useEffect } from 'react';
import { addCategory, deleteCategory, fetchCategories, fetchPagesCountByCategory } from '../../Api/wikiApi'; // Assuming these endpoints exist
import './Style/categorypagestyle.css';
import { useStyleContext } from '../../Components/contexts/StyleContext';

const EditCategoriesPage = ({ setAppCategories, cookies }) => {
    const [categories, setCategories] = useState([]);
    const [pagesByCategory, setPagesByCategory] = useState({});
    const [newCategory, setNewCategory] = useState('');
    const { styles } = useStyleContext();

    useEffect(() => {
      fetchCategories()
          .then(async categories => {
            console.log(categories);
            setCategories(categories);
          })
          .catch(error => {
              console.error('Error fetching categories:', error);
          });
    }, []);

    useEffect(() => {
      const fetchPagesCounts = async () => {
          if (categories.length > 0) {
              const pagesCountPromises = categories.map(async (category) => {
                  const count = await fetchPagesCountByCategory(category.id);
                  return { [category.id]: count };
              });
              const pagesCounts = await Promise.all(pagesCountPromises);
              const pagesCountMap = pagesCounts.reduce((acc, cur) => ({ ...acc, ...cur }), {});
              setPagesByCategory(pagesCountMap);
          }
      };
      fetchPagesCounts();
  }, [categories]);

    const handleChange = (event) => {
        setNewCategory(event.target.value);
    };

    const handleAddCategory = async () => {
        try {
            const addedCategory = await addCategory(newCategory, cookies);
            if (addedCategory) {
                const updatedCategories = [...categories, addedCategory];
                setCategories(updatedCategories);
                setAppCategories(updatedCategories);
                setPagesByCategory({ ...pagesByCategory, [addedCategory.id]: 0 });
            } else {
                console.error(`Failed to add category ${newCategory}.`);
            }
        } catch (error) {
            console.error('Error adding category:', error.message);
        }
    };

    const handleDeleteCategory = async (category) => {
        try {
            const categoryId = category.id;
            const status = await deleteCategory(categoryId, cookies);
            if (status === 204 || status === 200) {
                console.log(`Category ${category.name} deleted successfully.`);
                const updatedCategories = categories.filter(cat => cat.id !== categoryId);
                setCategories(updatedCategories);
                setAppCategories(updatedCategories);
                const { [categoryId]: _, ...updatedPagesByCategory } = pagesByCategory;
                setPagesByCategory(updatedPagesByCategory);
            } else {
                console.error(`Failed to delete category ${category.name}. Status: ${status}`);
            }
        } catch (error) {
            console.error('Error deleting category:', error.message);
        }
    };

    return (
        <div className="article" style={{ backgroundColor: styles.articleColor }}>
            <p className='cat-text'>Categories:</p>
            <ul>
                {categories.map((category, index) => (
                    <li key={index}>
                        <div className='category-row'>
                            <span>{category.categoryName}</span>
                            {pagesByCategory[category.id] === 0 && (
                                <button onClick={() => handleDeleteCategory(category)}>Delete Category</button>
                            )}
                        </div>
                    </li>
                ))}
            </ul>
            <div>
                <input
                    type="text"
                    value={newCategory}
                    onChange={handleChange}
                    placeholder="Enter new category"
                />
                <button onClick={handleAddCategory}>Add Category</button>
            </div>
        </div>
    );
};

export default EditCategoriesPage;
