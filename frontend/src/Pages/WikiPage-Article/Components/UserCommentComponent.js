import React, { useState } from 'react';
import { format, parseISO  } from 'date-fns';
import WikiPageReplyComponent from './WikiPageReplyComponent';
import DisplayProfileImageElement from '../../ProfilePage/Components/DisplayProfileImageElement';


const UserCommentComponent = ({ comment, user, cookies, handleCommentSubmit, postComment, page, index, showRepliesIndex, toggleRepliesIndex }) => {
    const [editingCommentIndex, setEditingCommentIndex] = useState(null);
    const [editedComment, setEditedComment] = useState("");
    const [showReplyBox, setShowReplyBox] = useState(false);

    const handleEditClick = (initialContent) => {
        setEditingCommentIndex(comment.id);
        setEditedComment(initialContent);
    };

    const handleCancelEdit = () => {
        setEditingCommentIndex(null);
        setEditedComment("");
    };

    const handleCommentEditSubmit = () => {
        // Submit edited comment logic goes here
        setEditingCommentIndex(null);
        setEditedComment("");
    };

    const formatDate = (dateString) => {
        // Create a Date object from the input date string (assuming dateString is in ISO 8601 format)
        const date = new Date(dateString);

        // Get the components of the date (year, month, day, hour, minute)
        const year = date.getFullYear();
        const month = ('0' + (date.getMonth() + 1)).slice(-2); // Months are zero indexed
        const day = ('0' + date.getDate()).slice(-2);
        const hours = ('0' + date.getHours()).slice(-2);
        const minutes = ('0' + date.getMinutes()).slice(-2);

        // Construct the formatted date string
        const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}`;

        return formattedDate;
    };

    return (
        <div className='wikipage-comment'>
            <div className='wikipage-comment-profilepic'>
                <DisplayProfileImageElement profilePicture={comment.userProfile.profilePicture} classNameProp={'wikipage-comment-profilepic'} />
            </div>
            <div className='wikipage-comment-content'>
                <div className='wikipage-comment-data'>
                    <a href={`/profile/${comment.userProfile.userName}`}>
                        <span>{comment.userProfile.displayName}</span>
                        <span> ({comment.userProfile.userName})</span>
                    </a>
                    {" | "}
                    <span>{formatDate(comment.postDate)}</span>
                </div>
                <div className='wikipage-comment-text'>
                    {editingCommentIndex === index ? (
                        <textarea value={editedComment} onChange={(e) => setEditedComment(e.target.value)} />
                        ) : (
                        <p>{comment.content} {comment.isEdited && "(edited)"}</p>
                        )}
                </div>
                { user && (<div>
                    <a href="#" onClick={() => setShowReplyBox(!showReplyBox)}> Reply</a>
                </div>)}
                {showReplyBox && (
                    <WikiPageReplyComponent user={user} page={page} cookies={cookies} handleCommentSubmit={handleCommentSubmit} postComment={postComment} replyTo={comment} />
                )}
                {comment.replies && comment.replies.length > 0 &&
                    <a href="#" onClick={() => toggleRepliesIndex(index)}>{showRepliesIndex[index] ? "Hide Replies" : "Show Replies"}</a>
                }
                {showRepliesIndex[index] && comment.replies && comment.replies.length > 0 &&
                    comment.replies.map((reply, replyIndex) => (
                        <div key={replyIndex} className='wikipage-reply'>
                            <div className='wikipage-reply-profile'>
                                <div className='wikipage-comment-profilepic'>
                                    <DisplayProfileImageElement profilePicture={reply.userProfile.profilePicture} classNameProp={'reply-comment-profilepic'} />
                                </div>
                            </div>
                            <div className='wikipage-reply-content'>
                                <div className='wikipage-comment-data'>
                                    <a href={`/profile/${reply.userProfile.userName}`}>
                                        <span>{reply.userProfile.displayName}</span>
                                        <span> ({reply.userProfile.userName})</span>
                                    </a>
                                </div>
                                <div className='wikipage-comment-text'>
                                    <p>{reply.content}</p>
                                </div>
                            </div>
                        </div>
                    ))
                }
            </div>
        </div>
    );
};

export default UserCommentComponent;
