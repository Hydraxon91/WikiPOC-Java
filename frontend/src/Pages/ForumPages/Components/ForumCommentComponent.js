import React, { useEffect, useState } from 'react';
import { useUserContext } from '../../../Components/contexts/UserContextProvider';
import { getUserProfileByUsername } from '../../../Api/wikiUserApi';
import { postForumComment } from '../../../Api/forumApi';
import { Link } from 'react-router-dom';
import { format } from 'date-fns';
import DisplayProfileImageElement from '../../ProfilePage/Components/DisplayProfileImageElement';
import ForumSubmitCommentComponent from './ForumSubmitCommentComponent';
import { useStyleContext } from '../../../Components/contexts/StyleContext';
import "../Styles/forumpost.css";

const ForumCommentComponent = ({ post, cookies, isPopupVisible, togglePopupVisibility, quotedPostId, setQuotedPostMethod }) => {
    const { decodedTokenContext } = useUserContext();
    const [user, setUser] = useState();
    const [currPost, setCurrPost] = useState(post);
    const { styles } = useStyleContext();
    const maxQuoteDepth = 1;

    const [currentPage, setCurrentPage] = useState(1);
    const [commentsPerPage] = useState(20);

    useEffect(() => {
        const decodedTokenName = decodedTokenContext?.["http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"];
        if (decodedTokenName) {
            getUserProfileByUsername(decodedTokenName, setUser);
        }
    }, [decodedTokenContext]);

    useEffect(() => {
        setCurrPost(post);
    }, [post]);

    const handleCommentSubmit = (newComment) => {
        const postDate = newComment.postDate.endsWith('Z') ? newComment.postDate.slice(0, -1) : newComment.postDate;
        setCurrPost((currPost) => ({
            ...currPost,
            comments: [...currPost.comments, { ...newComment, postDate }],
        }));
    };

    const formatDate = (dateString) => {
        const utcDate = new Date(dateString + 'Z');
        const formattedDate = format(utcDate, 'EEEE, dd MMM yyyy, HH:mm');
        return formattedDate.replace(/\//g, '-');
    };

    const renderQuote = (comment, currentDepth, maxDepth) => {
        var replyComment = currPost.comments.find(c => c.id === comment.replyToCommentId);
        if (!replyComment || currentDepth > maxDepth) {
            return null;
        }
        
        return (
            <div className="quoted-comment">
                <p>{replyComment.userProfile.displayName} wrote:</p>
                <div dangerouslySetInnerHTML={{ __html: replyComment.content }} />
                {renderQuote(replyComment, currentDepth + 1, maxDepth)}
            </div>
        );
    };

    const indexOfLastComment = currentPage * commentsPerPage;
    const indexOfFirstComment = indexOfLastComment - commentsPerPage;
    const currentComments = currPost.comments.slice(indexOfFirstComment, indexOfLastComment);
    const totalPages = Math.ceil(currPost.comments.length / commentsPerPage);


    const renderPagination = (totalPages, currentPage, setCurrentPage, postsPerPage, postsLength) => {
        const indexOfLastPost = currentPage * postsPerPage;
    
        return (
            <div className="pagination">
                {totalPages > 1 && (
                    <>
                        <button onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 1}>Previous</button>
                        {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
                            <>
                                {(page === 1 || page === totalPages || (page >= currentPage - 2 && page <= currentPage + 2)) && (
                                    <button key={page} onClick={() => setCurrentPage(page)} className={currentPage === page ? "active" : ""}>{page}</button>
                                )}
                                {page === 1 && currentPage >= 5 && <span>...</span>}
                            </>
                        ))}
                        <button onClick={() => setCurrentPage(currentPage + 1)} disabled={indexOfLastPost >= postsLength}>Next</button>
                    </>
                )}
            </div>
        );
    };

    return (
        <>
            {currPost && (
                <div>
                    {currentComments.map((comment, index) => (
                        <div key={index}>
                            {/* {console.log(comment)} */}
                            <div className="fp-grid">
                                <div className="fp-grid-row">
                                    <div className="fp-grid-cell"><Link to={`/profile/${comment.userProfile.userName}`}>{comment.userProfile.displayName}</Link></div>
                                    <div className="fp-grid-cell firstrow">
                                        <div className='fp-grid-cell-left'>Post Subject: re: {post.postTitle}</div>
                                        <div className='fp-grid-cell-right'>Posted: {formatDate(comment.postDate)}</div>
                                        <button className="quote-button" style={{ backgroundColor: styles.articleColor }} onClick={() => setQuotedPostMethod(comment)}>
                                            Quote
                                        </button>
                                    </div>
                                </div>
                                <div className="fp-grid-row">
                                    <div className="fp-grid-cell">
                                        {comment.userProfile?.profilePicture && <DisplayProfileImageElement profilePicture={comment.userProfile.profilePicture} />}
                                    </div>
                                    <div className="fp-grid-cell">
                                        {renderQuote(comment, 0, maxQuoteDepth)}
                                        <div dangerouslySetInnerHTML={{ __html: comment.content }}></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                    {renderPagination(totalPages, currentPage, setCurrentPage)}
                    {isPopupVisible && user && <ForumSubmitCommentComponent user={user} page={currPost} cookies={cookies}
                        handleCommentSubmit={handleCommentSubmit} postComment={postForumComment}
                        togglePopupVisibility={togglePopupVisibility} quotedPostId={quotedPostId}
                    />}
                </div>
            )}
        </>
    );
};

export default ForumCommentComponent;
