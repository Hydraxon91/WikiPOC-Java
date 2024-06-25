package com.hydraxon91.backend.models.ArticleModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hydraxon91.backend.models.UserModels.Comment;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ARTICLE_COMMENT")
public class ArticleComment extends Comment {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_page_id", referencedColumnName = "id", nullable = false)
    private ArticlePage articlePage;

    public ArticlePage getArticlePage() {
        return articlePage;
    }

    public void setArticlePage(ArticlePage articlePage) {
        this.articlePage = articlePage;
    }
    
}
