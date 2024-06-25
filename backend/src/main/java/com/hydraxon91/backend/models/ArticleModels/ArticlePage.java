package com.hydraxon91.backend.models.ArticleModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hydraxon91.backend.models.UserModels.UserComment;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class ArticlePage {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String title;
    private String siteSub;
    private String roleNote;
    private String content;

    @Column(nullable = true)
    private UUID categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private Category category;

    private LocalDateTime postDate;
    private LocalDateTime lastUpdateDate;
    private boolean legacyArticlePage = false;
    
    @OneToMany(mappedBy = "article-page", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paragraph> paragraphs = new ArrayList<>();

    @OneToMany(mappedBy = "article-page", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserComment> comments = new ArrayList<>();

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public boolean isLegacyArticlePage() {
        return legacyArticlePage;
    }

    public void setLegacyArticlePage(boolean legacyArticlePage) {
        this.legacyArticlePage = legacyArticlePage;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public List<UserComment> getComments() {
        return comments;
    }

    public void setComments(List<UserComment> comments) {
        this.comments = comments;
    }
    
}
