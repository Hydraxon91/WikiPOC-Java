import React, { useEffect, useState } from 'react';
import { useStyleContext } from '../../Components/contexts/StyleContext';
import { Link } from 'react-router-dom';
import { getForumTopics } from '../../Api/forumApi';
import { format } from 'date-fns';
import Breadcrumbs from './Components/Breadcrumbs';
import './Styles/forumlandingpage.css';

const ForumLandingPage = () => {
    const [topics, setTopics] = useState([]);
    const {styles} = useStyleContext();

    useEffect(() => {
        fetchForumTopics();
    }, []);

    // useEffect(() => {
    //     console.log(topics);
    // }, [topics]);
    
    const getCommentsLength = (topic) =>{
        var counter = 0;
        topic.forumPosts.forEach(post => {
            counter += post.comments.length;
        });
        return counter;
    }

    const getLatestComment = (topic) => {
        let latestComment = null;
        topic.forumPosts.forEach(post => {
            // Check if the forum post itself is the latest
            if (!latestComment || (post.postDate && new Date(post.postDate) > new Date(latestComment.postDate))) {
                latestComment = post;
            }
            // Check each comment within the post
            post.comments.forEach(comment => {
                if (!latestComment || new Date(comment.postDate) > new Date(latestComment.postDate)) {
                    latestComment = comment;
                }
            });
        });
        
        if (!latestComment) {
            return (
                <div>No comments yet</div>
            );
        }
    
        const userProfile = latestComment.userProfile ? latestComment.userProfile : latestComment.user; 
    
        // Parse the date string as UTC
        const utcDate = new Date(latestComment.postDate + 'Z');
        // Calculate time difference in minutes
        const diffInMinutes = Math.floor((new Date() - utcDate) / (1000 * 60));
        // Format the zoned date
        const formattedDate = diffInMinutes < 60 ? `${diffInMinutes} minutes ago` : format(utcDate, 'EEEE, dd MMM yyyy, HH:mm');
    
        return (
            <>
                <div>{formattedDate}</div>
                <Link to={`/profile/${userProfile.userName}`}><div>{userProfile.displayName}</div></Link>
            </>
        );
    };
    

    const fetchForumTopics = async () =>{
        try {
            const fetchedTopics = await getForumTopics();
            setTopics(fetchedTopics);
        } catch (error) {
            console.error("Error fetching Topics:", error);
        }
    };

    return (
        <div className='forum-mainsection'> 
        <Breadcrumbs/>
        <div className="forum-grid article" style={{backgroundColor: styles.articleColor}}>
            <div className="grid-header">
                <div className="header-cell">Forum</div>
                <div className="header-cell">Topics</div>
                <div className="header-cell">Posts</div>
                <div className="header-cell">Last Post</div>
            </div>
            {topics.map(topic => (
                <div className="grid-row" key={topic.id}>
                    <div className="grid-cell title">
                        <Link to={`/forum/${topic.slug}`}><div className='topicTitle'>{topic.title}</div></Link>
                        <div>{topic.description}</div>
                    </div>
                    <div className="grid-cell">{topic.forumPosts.length}</div>
                    <div className="grid-cell">{topic.forumPosts.length + getCommentsLength(topic)}</div>
                    <div className="grid-cell">{getLatestComment(topic)}</div>
                </div>
            ))}
        </div>
        </div>
    );
}

export default ForumLandingPage;