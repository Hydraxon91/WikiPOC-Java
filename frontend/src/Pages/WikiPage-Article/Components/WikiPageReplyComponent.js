import React, { useState } from 'react';
import DisplayProfileImageElement from '../../ProfilePage/Components/DisplayProfileImageElement';
import '../Style/commentreply.css'

const WikiPageReplyComponent = ({ user, page, cookies, handleCommentSubmit, postComment, replyTo, showReplyBoxRemoveIndex, index }) => {
    const [commentText, setCommentText] = useState('');

    const handleCommentChange = (event) => {
        setCommentText(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const newComment = {
            content: commentText,
            userProfileId: user.id,
            wikiPageId: page.id,
            postDate: new Date().toISOString(),
            replyToCommentId: replyTo.id,
            isEdited: false,
        };

        try {
            await postComment(newComment, cookies, user);
            setCommentText('');
            newComment.userProfile = user;
            handleCommentSubmit(newComment);
            // Ensure replyTo.replies is initialized
            if (!replyTo.replies) {
                replyTo.replies = [];
            }

            // Add newComment to replyTo.replies
            replyTo.replies.push(newComment);
            alert('Successfully submitted comment');
        } catch (error) {
            console.error('Error posting comment:', error);
        }
    };

    return (
        <div className="reply-container">
            <div className="reply-comment-write-container">
                <div className="reply-profile-pic">
                    <DisplayProfileImageElement profilePicture={user.profilePicture} classNameProp={'reply-comment-profilepic'}/>
                </div>
                <div className="reply-comment-write-textarea">
                    <textarea
                        placeholder="Add a reply..."
                        maxLength="1500"
                        name="comment"
                        value={commentText}
                        onChange={handleCommentChange}
                    ></textarea>
                </div>
            </div>
            <div className="reply-buttons">
                <button className="reply-button cancel" onClick={() => showReplyBoxRemoveIndex(index)}>
                    Cancel
                </button>
                <button className="reply-button send" onClick={handleSubmit}>
                    Reply
                </button>
            </div>
        </div>
    );
};

export default WikiPageReplyComponent;
