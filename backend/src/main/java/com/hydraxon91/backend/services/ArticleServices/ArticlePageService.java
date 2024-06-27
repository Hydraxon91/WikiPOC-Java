package com.hydraxon91.backend.services.ArticleServices;

import com.hydraxon91.backend.models.ArticleModels.ArticlePage;
import com.hydraxon91.backend.models.ArticleModels.ArticlePageTitleAndIdProjection;
import com.hydraxon91.backend.models.ArticleModels.Paragraph;
import com.hydraxon91.backend.models.Forms.ArticlePageWithImagesInputModel;
import com.hydraxon91.backend.models.Forms.ImageFormModel;
import com.hydraxon91.backend.models.Forms.WPWithImagesOutputModel;
import com.hydraxon91.backend.repositories.ArticleRepositories.ArticlePageRepository;
import com.hydraxon91.backend.repositories.ArticleRepositories.ParagraphRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticlePageService {

    private final ArticlePageRepository articlePageRepository;
    private final ParagraphRepository paragraphRepository;

    @Value("${pictures.path-container}")
    private String picturesPathContainer;

    @Autowired
    public ArticlePageService(ArticlePageRepository articlePageRepository, ParagraphRepository paragraphRepository) {
        this.articlePageRepository = articlePageRepository;
        this.paragraphRepository = paragraphRepository;
    }

    public List<ArticlePage> getAllArticlePages() {
        return articlePageRepository.findByArchivedIsFalseAndApprovedIsTrue();
    }

    public List<ArticlePage> getUnapprovedUserSubmittedNewPages() {
        return articlePageRepository.findByApprovedIsFalseAndIsNewPageTrue();
    }

    public List<ArticlePage> getUnapprovedUserSubmittedUpdates() {
        return articlePageRepository.findByApprovedIsFalseAndIsNewPageFalse();
    }
    
    public List<ArticlePageTitleAndIdProjection> getUnapprovedNewPageTitlesAndIds() {
        return articlePageRepository.findTitlesAndIdsByApprovedIsFalseAndIsNewPageTrue();
    }

    public List<ArticlePageTitleAndIdProjection> getUnapprovedUpdateTitlesAndIds() {
        return articlePageRepository.findTitlesAndIdsByApprovedIsFalseAndIsNewPageFalse();
    }

    public WPWithImagesOutputModel getArticlePageById(UUID id) throws IOException {
        ArticlePage articlePage = articlePageRepository.findById(id)
                .orElseThrow(() -> new IOException("ArticlePage not found with id: " + id));

        List<ImageFormModel> images = fetchImagesForArticlePage(articlePage);

        WPWithImagesOutputModel wpWithImagesOutputModel = new WPWithImagesOutputModel();
        wpWithImagesOutputModel.setArticlePage(articlePage);
        wpWithImagesOutputModel.setImages(images);

        return wpWithImagesOutputModel;
    }

    public WPWithImagesOutputModel getArticleBySlug(String slug) throws IOException {
        ArticlePage articlePage = articlePageRepository.findBySlug(slug)
                .orElseThrow(() -> new IOException("ArticlePage not found with slug: " + slug));

        List<ImageFormModel> images = fetchImagesForArticlePage(articlePage);

        WPWithImagesOutputModel wpWithImagesOutputModel = new WPWithImagesOutputModel();
        wpWithImagesOutputModel.setArticlePage(articlePage);
        wpWithImagesOutputModel.setImages(images);

        return wpWithImagesOutputModel;
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

    @Transactional(rollbackFor = Exception.class)
    public ArticlePage createArticlePageAdmin(ArticlePageWithImagesInputModel inputModel) throws IOException {
        ArticlePage articlePage = inputModel.getArticlePage();

        // Save the article page
        String slug = generateSlug(articlePage.getTitle());
        articlePage.setSlug(slug);
        articlePage.setApproved(true);
        articlePage.setNewPage(false);
        articlePage = articlePageRepository.save(articlePage);

        // Save the images
        Collection<ImageFormModel> images = inputModel.getImages();
        if (images != null && !images.isEmpty()) {
            for (ImageFormModel image : images) {
                saveImage(image, articlePage.getId());
            }
        }
        return articlePage;
    }

    @Transactional(rollbackFor = Exception.class)
    public ArticlePage createArticlePageUser(ArticlePageWithImagesInputModel inputModel) throws IOException {
        ArticlePage articlePage = inputModel.getArticlePage();

        // Save the article page
        String slug = generateSlug(articlePage.getTitle());
        articlePage.setSlug(slug);
        articlePage.setApproved(false);
        articlePage = articlePageRepository.save(articlePage);
        
        // Save the images
        Collection<ImageFormModel> images = inputModel.getImages();
        if (images != null && !images.isEmpty()) {
            for (ImageFormModel image : images) {
                saveImage(image, articlePage.getId());
            }
        }
        
        return articlePage;
    }

    @Transactional(rollbackFor = Exception.class)
    public ArticlePage updateArticlePage(UUID id, ArticlePageWithImagesInputModel inputModel) throws IOException {
        ArticlePage updatedArticlePage = inputModel.getArticlePage();

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
            String directoryPath = picturesPathContainer + "/articles/" + existingArticlePage.getId();
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // List existing files in the directory
            List<String> existingFiles = Arrays.asList(directory.list());

            // Get the list of new image filenames
            Collection<ImageFormModel> images = inputModel.getImages();
            List<String> newImageFiles = images.stream().map(ImageFormModel::getFileName).collect(Collectors.toList());

            // Delete unused images
            deleteUnusedImages(existingFiles, newImageFiles, directoryPath);

            // Save the images
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
            String directoryPath = picturesPathContainer + "/articles/" + existingArticlePage.getId();
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

    @Transactional(rollbackFor = Exception.class)
    public boolean archiveArticlePage(UUID id) {
        Optional<ArticlePage> existingArticlePageOpt = articlePageRepository.findById(id);
        if (existingArticlePageOpt.isPresent()) {
            ArticlePage existingArticlePage = existingArticlePageOpt.get();
            existingArticlePage.setArchived(true); // Set archived to true instead of deleting
            articlePageRepository.save(existingArticlePage);

            // Optionally, handle deletion of associated files or other cleanup here

            return true;
        } else {
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean acceptUserSubmittedUpdate(UUID originalArticleId, UUID userSubmittedUpdateId) {
        // Retrieve the original article
        ArticlePage originalArticle = articlePageRepository.findById(originalArticleId)
                .orElseThrow(() -> new EntityNotFoundException("Original article not found with id: " + originalArticleId));

        // Archive the original article instead of deleting it
        originalArticle.setArchived(true);
        articlePageRepository.save(originalArticle);

        // Retrieve the user-submitted article
        ArticlePage userSubmittedArticle = articlePageRepository.findById(userSubmittedUpdateId)
                .orElseThrow(() -> new EntityNotFoundException("User-submitted article not found with id: " + userSubmittedUpdateId));

        // Set approved status and preserve original title
        userSubmittedArticle.setApproved(true);
        userSubmittedArticle.setTitle(originalArticle.getTitle());

        // Check if the original slug exists
        String originalSlug = originalArticle.getSlug();
        String newSlug = originalSlug;

        if (articlePageRepository.existsBySlug(originalSlug)) {
            // Generate a new unique slug if the original slug exists
            int suffix = 1;
            while (articlePageRepository.existsBySlug(newSlug)) {
                newSlug = originalSlug + "-" + suffix++;
            }
        }

        // Set the new unique slug
        userSubmittedArticle.setSlug(newSlug);

        // Save the updated user-submitted article
        articlePageRepository.save(userSubmittedArticle);

        return true;
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
        String directoryPath = picturesPathContainer + "/articles/" + articlePageId;
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

    private List<ImageFormModel> fetchImagesForArticlePage(ArticlePage articlePage) throws IOException {
        String directoryPath = picturesPathContainer + "/articles/" + articlePage.getId();
        File directory = new File(directoryPath);
        if (directory.exists()) {
            File[] imageFiles = directory.listFiles();
            if (imageFiles != null) {
                return Arrays.stream(imageFiles)
                        .map(file -> {
                            try {
                                byte[] imageData = Files.readAllBytes(file.toPath());
                                String fileName = file.getName();
                                // Get file extension without using FilenameUtils
                                String extension = StringUtils.getFilenameExtension(fileName);

                                // Construct dataURL
                                String dataURL = "data:image/" + extension + ";base64,"
                                        + Base64.getEncoder().encodeToString(imageData);
                                ImageFormModel imageFormModel = new ImageFormModel();
                                imageFormModel.setFileName(fileName);
                                imageFormModel.setDataURL(dataURL);
                                return imageFormModel;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }
}
