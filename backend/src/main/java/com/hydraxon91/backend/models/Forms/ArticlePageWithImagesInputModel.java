package com.hydraxon91.backend.models.Forms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import com.hydraxon91.backend.models.ArticleModels.UserSubmittedArticlePage;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

public class ArticlePageWithImagesInputModel {
    // Normal article page properties
    private String title;
    private String siteSub;
    private String roleNote;
    private String content;
    private UUID categoryId;
    private Date postDate;
    private Date lastUpdateDate;
    private boolean legacyArticlePage = false;

    // If it's a user-submitted article page
    private UUID parentArticlePageId;
    @JsonIgnore
    private ArticlePage parentArticlePage;
    private String submittedBy;
    private Boolean approved;
    private Boolean isNewPage;

    // Image data
    private Collection<ImageFormModel> images = new ArrayList<>();

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSiteSub() {
        return siteSub;
    }

    public void setSiteSub(String siteSub) {
        this.siteSub = siteSub;
    }

    public String getRoleNote() {
        return roleNote;
    }

    public void setRoleNote(String roleNote) {
        this.roleNote = roleNote;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
    

    public boolean isLegacyArticlePage() {
        return legacyArticlePage;
    }

    public void setLegacyArticlePage(boolean legacyArticlePage) {
        this.legacyArticlePage = legacyArticlePage;
    }

    public UUID getParentArticlePageId() {
        return parentArticlePageId;
    }

    public void setArticlePageId(UUID articlePageId) {
        this.parentArticlePageId = articlePageId;
    }

    public ArticlePage getPArentArticlePage() {
        return parentArticlePage;
    }

    public ArticlePage getArticlePage() {
        ArticlePage articlePage;

        if (this.parentArticlePage != null) {
            // Assuming UserSubmittedArticlePage extends ArticlePage
            articlePage = new UserSubmittedArticlePage();
        } else {
            articlePage = new ArticlePage();
        }

        // Set common properties from ArticlePageWithImagesInputModel
        articlePage.setTitle(this.title);
        articlePage.setSiteSub(this.siteSub);
        articlePage.setRoleNote(this.roleNote);
        articlePage.setContent(this.content);
        articlePage.setCategoryId(this.categoryId);

        // Convert Date to LocalDateTime (if necessary)
        if (this.postDate != null) {
            articlePage.setPostDate(this.postDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        if (this.lastUpdateDate != null) {
            articlePage.setLastUpdateDate(this.lastUpdateDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }

        articlePage.setLegacyArticlePage(this.legacyArticlePage);
        articlePage.setApproved(this.approved != null ? this.approved : false); // Default to false if null
        articlePage.setNewPage(this.isNewPage != null ? this.isNewPage : false); // Default to false if null

        return articlePage;
    }

    public void setParentArticlePage(ArticlePage articlePage) {
        this.parentArticlePage = articlePage;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean getIsNewPage() {
        return isNewPage;
    }

    public void setIsNewPage(Boolean isNewPage) {
        this.isNewPage = isNewPage;
    }

    public Collection<ImageFormModel> getImages() {
        return images;
    }

    public void setImages(Collection<ImageFormModel> images) {
        this.images = images;
    }
}
