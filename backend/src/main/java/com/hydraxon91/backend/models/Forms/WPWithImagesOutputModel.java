package com.hydraxon91.backend.models.Forms;

import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import com.hydraxon91.backend.models.ArticleModels.UserSubmittedArticlePage;

import java.util.List;

public class WPWithImagesOutputModel {

    private ArticlePage articlePage;
    private UserSubmittedArticlePage userSubmittedArticlePage;
    private List<ImageFormModel> images;

    // Getters and setters
    public ArticlePage getArticlePage() {
        return articlePage;
    }

    public void setArticlePage(ArticlePage articlePage) {
        this.articlePage = articlePage;
    }

    public UserSubmittedArticlePage getUserSubmittedArticlePage() {
        return userSubmittedArticlePage;
    }

    public void setUserSubmittedArticlePage(UserSubmittedArticlePage userSubmittedArticlePage) {
        this.userSubmittedArticlePage = userSubmittedArticlePage;
    }

    public List<ImageFormModel> getImages() {
        return images;
    }

    public void setImages(List<ImageFormModel> images) {
        this.images = images;
    }
}
