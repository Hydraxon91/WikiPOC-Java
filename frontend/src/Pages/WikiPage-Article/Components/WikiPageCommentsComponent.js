import React, { useEffect, useState } from 'react';
import { useUserContext } from '../../../Components/contexts/UserContextProvider';
import { getUserProfileByUsername, postEditedComment, postComment } from '../../../Api/wikiUserApi';
import WikiPageSubmitCommentComponent from './WikiPageSubmitCommentComponent';
import UserCommentComponent from './UserCommentComponent';

const WikiPageCommentsComponent = ({ page, cookies, activeTab }) => {
    const { decodedTokenContext } = useUserContext();
    const [user, setUser] = useState();
    const [currPage, setCurrPage] = useState(page);
    const [showRepliesIndex, setShowRepliesIndex] = useState({});

    useEffect(() => {
        const decodedTokenName = decodedTokenContext?.["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"];
        if (decodedTokenName) {
            getUserProfileByUsername(decodedTokenName, setUser);
        }
    }, [decodedTokenContext]);

    useEffect(() => {
        setCurrPage(page);
    }, [page]);

    const handleCommentSubmit = (newComment) => {
        // Logic for handling new comment submission goes here
        setCurrPage((currPage) => ({
            ...currPage,
            comments: [...currPage.comments, newComment],
        }));
    };

    const toggleRepliesIndex = (index) => {
        setShowRepliesIndex(prevState => ({
            ...prevState,
            [index]: !prevState[index]
        }));
    };

    return (
        <div className={activeTab === 'comments' ? 'wikipage-component' : 'wikipage-component wikipage-hidden'}>
            {currPage && (
                <div>
                    <h3>Comments</h3>
                    {user && <WikiPageSubmitCommentComponent user={user} page={currPage} cookies={cookies} handleCommentSubmit={handleCommentSubmit} postComment={postComment} />}
                    {currPage.comments.map((comment, index) => (
                        !comment.replyToCommentId && <UserCommentComponent
                            key={comment.id}
                            comment={comment}
                            user={user}
                            cookies={cookies}
                            handleCommentSubmit={handleCommentSubmit}
                            postComment={postComment}
                            page={page}
                            index={index}
                            showRepliesIndex={showRepliesIndex} // Pass showRepliesIndex as a prop
                            toggleRepliesIndex={toggleRepliesIndex} // Pass toggleRepliesIndex function as a prop
                        />
                    ))}
                </div>
            )}
        </div>
    );
};

export default WikiPageCommentsComponent;
