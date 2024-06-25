package com.hydraxon91.backend.models.UserModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Choose appropriate strategy
@DiscriminatorColumn(name = "comment_type", discriminatorType = DiscriminatorType.STRING)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    
    private String content;
    
    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    @Column(name = "user_profile_id", insertable = false, updatable = false)
    private UUID userProfileId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date postDate;

    @ManyToOne
    @JoinColumn(name = "reply_to_comment_id")
    @JsonIgnore
    private Comment replyToComment;

    @Column(name = "reply_to_comment_id", insertable = false, updatable = false)
    private UUID replyToCommentId;

    @OneToMany(mappedBy = "replyToComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    private boolean isEdited = false;

    // Required no-argument constructor
    public Comment() {
    }

    // Getters and Setters

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UUID getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(UUID userProfileId) {
        this.userProfileId = userProfileId;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Comment getReplyToComment() {
        return replyToComment;
    }

    public void setReplyToComment(Comment replyToComment) {
        this.replyToComment = replyToComment;
    }

    public UUID getReplyToCommentId() {
        return replyToCommentId;
    }

    public void setReplyToCommentId(UUID replyToCommentId) {
        this.replyToCommentId = replyToCommentId;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }
}
