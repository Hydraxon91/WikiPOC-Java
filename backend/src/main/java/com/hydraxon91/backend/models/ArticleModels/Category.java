package com.hydraxon91.backend.models.ArticleModels;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private UUID id;

    private String categoryName;
    
    public Category(String categoryName){
        this.categoryName = categoryName;
    }

    public Category(){}

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticlePage> articlePages = new ArrayList<>();

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ArticlePage> getArticlePages() {
        return articlePages;
    }

    public void setArticlePages(List<ArticlePage> articlePages) {
        this.articlePages = articlePages;
    }
}
