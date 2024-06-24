package com.hydraxon91.backend.models.StyleModel;

import jakarta.persistence.*;

@Entity
@Table(name = "style_model")
public class StyleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String logo;
    private String wikiName;
    private String bodyColor;
    private String articleRightColor;
    private String articleRightInnerColor;
    private String articleColor;
    private String footerListLinkTextColor;
    private String footerListTextColor;
    private String fontFamily;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWikiName() {
        return wikiName;
    }

    public void setWikiName(String wikiName) {
        this.wikiName = wikiName;
    }

    public String getBodyColor() {
        return bodyColor;
    }

    public void setBodyColor(String bodyColor) {
        this.bodyColor = bodyColor;
    }

    public String getArticleRightColor() {
        return articleRightColor;
    }

    public void setArticleRightColor(String articleRightColor) {
        this.articleRightColor = articleRightColor;
    }

    public String getArticleRightInnerColor() {
        return articleRightInnerColor;
    }

    public void setArticleRightInnerColor(String articleRightInnerColor) {
        this.articleRightInnerColor = articleRightInnerColor;
    }

    public String getArticleColor() {
        return articleColor;
    }

    public void setArticleColor(String articleColor) {
        this.articleColor = articleColor;
    }

    public String getFooterListLinkTextColor() {
        return footerListLinkTextColor;
    }

    public void setFooterListLinkTextColor(String footerListLinkTextColor) {
        this.footerListLinkTextColor = footerListLinkTextColor;
    }

    public String getFooterListTextColor() {
        return footerListTextColor;
    }

    public void setFooterListTextColor(String footerListTextColor) {
        this.footerListTextColor = footerListTextColor;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }
    
}
