package com.hydraxon91.backend.models.ArticleModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.UUID;
import jakarta.persistence.Entity;

@Entity
public class UserSubmittedArticlePage extends ArticlePage{

//    @Id
//    @GeneratedValue
//    private UUID userSubmittedArticlePageId;

    @Column(nullable = true)
    private UUID parentArticleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentArticleId", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_user_submitted_article_page_article_page"), insertable = false, updatable = false)
    @JsonIgnore
    private ArticlePage parentArticlePage;

    private String submittedBy;
    private boolean approved = false;
    private boolean isNewPage;

    // Constructors, getters, setters
//    public UUID getUserSubmittedArticlePageId() {
//        return userSubmittedArticlePageId;
//    }
//
//    public void setUserSubmittedArticlePageId(UUID userSubmittedArticlePageId) {
//        this.userSubmittedArticlePageId = userSubmittedArticlePageId;
//    }

    public ArticlePage getParentArticlePage() {
        return parentArticlePage;
    }

    public void setParentArticlePage(ArticlePage articlePage) {
        this.parentArticlePage = articlePage;
    }

    public UUID getParentArticlePageId() {
        return parentArticleId;
    }

    public void setParentArticleId(UUID id) {
        this.parentArticleId = id;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isNewPage() {
        return isNewPage;
    }

    public void setNewPage(boolean newPage) {
        isNewPage = newPage;
    }
}
