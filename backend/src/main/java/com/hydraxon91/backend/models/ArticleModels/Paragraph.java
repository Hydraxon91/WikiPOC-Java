package com.hydraxon91.backend.models.ArticleModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
public class Paragraph {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(nullable = false)
    private UUID articlePageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wikiPageId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private ArticlePage articlePage;

    private String title;
    private String content;
    private String paragraphImage;
    private String paragraphImageText;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getWikiPageId() {
        return articlePageId;
    }

    public void setArticlePageId(UUID articlePageId) {
        this.articlePageId = articlePageId;
    }

    public ArticlePage getArticlePage() {
        return articlePage;
    }

    public void setWikiPage(ArticlePage articlePage) {
        this.articlePage = articlePage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParagraphImage() {
        return paragraphImage;
    }

    public void setParagraphImage(String paragraphImage) {
        this.paragraphImage = paragraphImage;
    }

    public String getParagraphImageText() {
        return paragraphImageText;
    }

    public void setParagraphImageText(String paragraphImageText) {
        this.paragraphImageText = paragraphImageText;
    }
}
