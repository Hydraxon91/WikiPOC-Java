package com.hydraxon91.backend.models.ArticleModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import jakarta.persistence.Entity;

@Entity
public class UserSubmittedArticlePage extends ArticlePage{
    
    // Define foreign key and relationship with WikiPage
    private UUID articlePageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articlePageId", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_user_submitted_article_page_article_page"))
    @JsonIgnore
    private ArticlePage articlePage;

    private String submittedBy;
    private boolean approved = false;
    private boolean isNewPage;

    // Constructors, getters, setters
    public UUID getWikiPageId() {
        return articlePageId;
    }

    public void setWikiPageId(UUID articlePageId) {
        this.articlePageId = articlePageId;
    }

    public ArticlePage getWikiPage() {
        return articlePage;
    }

    public void setWikiPage(ArticlePage articlePage) {
        this.articlePage = articlePage;
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
