import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Link } from 'react-router-dom';
import { getForumPostBySlug } from '../../Api/forumApi';
import ForumCommentComponent from './Components/ForumCommentComponent';
import DisplayProfileImageElement from '../ProfilePage/Components/DisplayProfileImageElement';
import { format } from 'date-fns';
import Breadcrumbs from './Components/Breadcrumbs';
import { useStyleContext } from '../../Components/contexts/StyleContext';
import "./Styles/forumpost.css"

const ForumPost = ({cookies}) => {
    const [post, setPost] = useState(null);
    const { postSlug } = useParams();
    const {styles} = useStyleContext();
    const [quotedPostId, setQuotedPostId] = useState(null);
    const [isPopupVisible, setIsPopupVisible] = useState(false);

    useEffect(() => {
        const fetchForumPost = async () => {
            try {
                const fetchedPost = await getForumPostBySlug(postSlug);
                setPost(fetchedPost);
            } catch (error) {
                console.error("Error fetching post:", error);
            }
        };

        fetchForumPost();
    }, [postSlug]);

    const togglePopupVisibility = () => {
        if (!cookies) {
            alert('You need to log in to post a reply.');
        } else {
            setIsPopupVisible(!isPopupVisible);
        }
    };

    const setQuotedPostMethod = (comment) => {
        if (!cookies) {
            alert('You need to log in to post a reply.');
        } else {
            setQuotedPostId(comment.id);
            setIsPopupVisible(!isPopupVisible);
        }
    }

    if (!post) {
        return <div>Loading...</div>;
    }

    return (
        <div className='forum-mainsection'>
            <Breadcrumbs/>
            <button className="modular-button" style={{backgroundColor: styles.articleColor}} onClick={togglePopupVisibility}>
                Post Reply
            </button>
            <ForumCommentComponent post={post} cookies={cookies} isPopupVisible={isPopupVisible} 
                togglePopupVisibility={togglePopupVisibility} quotedPostId={quotedPostId} setQuotedPostMethod={setQuotedPostMethod}
            />
        </div>
    );
};

export default ForumPost;