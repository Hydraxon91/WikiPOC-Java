import React, { useState, useEffect } from 'react';
import ReactQuill from 'react-quill';
import "../Styles/forumsubmintcommentcomponent.css"

const ForumSubmitCommentComponent = ({ user, page, cookies, handleCommentSubmit, postComment, togglePopupVisibility, quotedPostId  }) => {
    const [commentText, setCommentText] = useState('');

    const handleCommentChange = (event) => {
        setCommentText(event);
    };

    useEffect(()=>{console.log(quotedPostId);},[])

    const handleSubmit = async (event) => {
        event.preventDefault();
        const newComment = {
            content: commentText,
            userProfileId: user.id,
            wikiPageId: page.id,
            postDate: new Date().toISOString(),
            isReply: false,
            replyToCommentId: quotedPostId,
            isEdited: false,
        };
        console.log(newComment);
        try {
            await postComment(newComment, cookies, user);
            setCommentText('');
            newComment.userProfile = user;
            handleCommentSubmit(newComment);
            alert('Successfully submitted comment');
            togglePopupVisibility();
        } catch (error) {
            console.error('Error posting comment:', error);
        }
    };

    // Define custom modules for ReactQuill
    const modules = {
        toolbar: [
            [{ 'header': '1'}, {'header': '2'}, { 'font': [] }],
            [{size: []}],
            ['bold', 'italic', 'underline', 'strike', 'blockquote'],
            [{'list': 'ordered'}, {'list': 'bullet'}, 
            {'indent': '-1'}, {'indent': '+1'}],
            ['link', 'image'],
            ['clean']
        ],
        clipboard: {
            matchVisual: false,
        }
    };

    // Define custom styles for ReactQuill
    const formats = [
        'header', 'font', 'size',
        'bold', 'italic', 'underline', 'strike', 'blockquote',
        'list', 'bullet', 'indent',
        'link', 'image'
    ];

    return (
        <div className='fp-custom-popup-overlay'>
            <div className="fp-custom-popup">
                <form autoComplete="off" method="post" encType="multipart/form-data" onSubmit={handleSubmit}>
                    <div className="fp-comment-write-container">
                        <div className="fp-comment-write-textarea">
                            <ReactQuill
                                theme="snow"
                                onChange={handleCommentChange}
                                value={commentText}
                                modules={modules}
                                formats={formats}
                            />
                        </div>
                    </div>
                    <button className="fp-comment-button" type="submit">
                        Submit Comment
                    </button>
                    <button className='fp-comment-button right' onClick={togglePopupVisibility}>Cancel</button>
                </form>
            </div>
        </div>
    );
};

export default ForumSubmitCommentComponent;
