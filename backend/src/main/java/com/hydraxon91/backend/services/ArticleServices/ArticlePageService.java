package com.hydraxon91.backend.services.ArticleServices;

import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import com.hydraxon91.backend.models.ArticleModels.Paragraph;
import com.hydraxon91.backend.models.Forms.ImageFormModel;
import com.hydraxon91.backend.repositories.ArticleRepositories.ArticlePageRepository;
import com.hydraxon91.backend.repositories.ArticleRepositories.ParagraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ArticlePageService {

    private final ArticlePageRepository articlePageRepository;
    private final ParagraphRepository paragraphRepository;

    @Autowired
    public ArticlePageService(ArticlePageRepository articlePageRepository, ParagraphRepository paragraphRepository) {
        this.articlePageRepository = articlePageRepository;
        this.paragraphRepository = paragraphRepository;
    }

    public List<ArticlePage> getAllArticlePages() {
        return articlePageRepository.findAll();
    }

    public ArticlePage getArticlePageById(UUID id) {
        return articlePageRepository.findById(id).orElse(null);
    }

    public Optional<ArticlePage> getArticleBySlug(String slug) {
        return articlePageRepository.findBySlug(slug);
    }

    public List<ArticlePage> findArticlePagesByCategoryId(UUID categoryId) {
        return articlePageRepository.findByCategoryId(categoryId);
    }

    public long countArticlePagesByCategoryId(UUID categoryId) {
        return articlePageRepository.countByCategoryId(categoryId);
    }

    public List<ArticlePageProjection> getArticleTitlesAndSlugs() {
        return articlePageRepository.findArticleTitlesAndSlugs();
    }

    public ArticlePage createArticlePage(ArticlePage articlePage, List<ImageFormModel> images) throws IOException {
        // Save the article page
        String slug = generateSlug(articlePage.getTitle());
        articlePage.setSlug(slug);
        articlePage = articlePageRepository.save(articlePage);

        // Save the images
        if (images != null && !images.isEmpty()) {
            for (ImageFormModel image : images) {
                saveImage(image, articlePage.getId());
            }
        }
        return articlePage;
    }

    public ArticlePage updateArticlePage(UUID id, ArticlePage updatedArticlePage, List<ImageFormModel> images) throws IOException {
        // Retrieve the existing ArticlePage
        Optional<ArticlePage> existingArticlePageOpt = articlePageRepository.findById(id);

        if (existingArticlePageOpt.isPresent()) {
            ArticlePage existingArticlePage = existingArticlePageOpt.get();

            // Update the fields of the existing ArticlePage
            existingArticlePage.setTitle(updatedArticlePage.getTitle());
            existingArticlePage.setContent(updatedArticlePage.getContent());
            existingArticlePage.setCategoryId(updatedArticlePage.getCategoryId());
            // Add more fields as needed

            // Handle the updating and saving of images
            String directoryPath = System.getenv("PICTURES_PATH_CONTAINER") + "/articles/" + existingArticlePage.getId();
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // List existing files in the directory
            List<String> existingFiles = Arrays.asList(directory.list());

            // Get the list of new image filenames
            List<String> newImageFiles = images.stream().map(ImageFormModel::getFileName).collect(Collectors.toList());

            // Delete unused images
            deleteUnusedImages(existingFiles, newImageFiles, directoryPath);

            // Save new images
            if (images != null && !images.isEmpty()) {
                for (ImageFormModel image : images) {
                    saveImage(image, existingArticlePage.getId());
                }
            }

            // Save the updated ArticlePage back to the repository
            return articlePageRepository.save(existingArticlePage);
        } else {
            throw new IllegalArgumentException("ArticlePage not found with id: " + id);
        }
    }

    public boolean deleteArticlePage(UUID id) {
        Optional<ArticlePage> existingArticlePageOpt = articlePageRepository.findById(id);
        if (existingArticlePageOpt.isPresent()) {
            ArticlePage existingArticlePage = existingArticlePageOpt.get();
            String directoryPath = System.getenv("PICTURES_PATH_CONTAINER") + "/articles/" + existingArticlePage.getId();
            File directory = new File(directoryPath);
            if (directory.exists()) {
                // List existing files in the directory
                List<String> existingFiles = Arrays.asList(directory.list());
                try {
                    // Delete all images
                    deleteUnusedImages(existingFiles, List.of(), directoryPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            articlePageRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    // Implement methods for handling paragraphs related to ArticlePage
    public List<Paragraph> getParagraphsByArticlePageId(UUID articlePageId) {
        // Implement method to fetch paragraphs by articlePageId
        return paragraphRepository.findByArticlePage_Id(articlePageId);
    }

    public String generateSlug(String title) {
        String baseSlug = title.toLowerCase().replaceAll("\\s+", "-");
        String finalSlug = baseSlug;
        int suffix = 1;

        while (isSlugExists(finalSlug)) {
            finalSlug = baseSlug + "-" + suffix++;
        }

        return finalSlug;
    }

    public boolean isSlugExists(String slug) {
        return articlePageRepository.existsBySlug(slug);
    }

    private void saveImage(ImageFormModel image, UUID articlePageId) throws IOException {
        String directoryPath = System.getenv("PICTURES_PATH_CONTAINER") + "/articles/" + articlePageId;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        byte[] imageData = java.util.Base64.getDecoder().decode(image.getDataURL().split(",")[1]);
        File file = new File(directoryPath + "/" + image.getFileName());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageData);
        }
    }

    private void deleteUnusedImages(List<String> existingFiles, List<String> newImageFiles, String directoryPath) throws IOException {
        for (String existingFile : existingFiles) {
            if (!newImageFiles.contains(existingFile)) {
                Files.deleteIfExists(new File(directoryPath, existingFile).toPath());
            }
        }
    }
}
