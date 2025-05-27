package ma.formation.springboot.article_sb.service;

import lombok.RequiredArgsConstructor;
import ma.formation.springboot.article_sb.model.Article;
import ma.formation.springboot.article_sb.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article create(Article article) {
        return articleRepository.save(article);
    }

    public Article update(Long id, Article article) {
        Optional<Article> existingArticle = articleRepository.findById(id);
        if (existingArticle.isPresent()) {
            Article updatedArticle = existingArticle.get();
            updatedArticle.setDescription(article.getDescription());
            updatedArticle.setPrice(article.getPrice());
            updatedArticle.setQuantite(article.getQuantite());
            return articleRepository.save(updatedArticle);
        } else {
            throw new IllegalArgumentException("Article with ID " + id + " not found");
        }
    }

    public void delete(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new IllegalArgumentException("Article with ID " + id + " not found");
        }
        articleRepository.deleteById(id);
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article with ID " + id + " not found"));
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    // New method for search functionality
    public List<Article> searchArticles(String description, Double minPrice, Double maxPrice,
                                        Double minQuantity, Double maxQuantity) {
        List<Article> articles = findAll();

        return articles.stream()
                .filter(article -> description == null || description.trim().isEmpty() ||
                        article.getDescription().toLowerCase().contains(description.toLowerCase()))
                .filter(article -> minPrice == null || article.getPrice() >= minPrice)
                .filter(article -> maxPrice == null || article.getPrice() <= maxPrice)
                .filter(article -> minQuantity == null || article.getQuantite() >= minQuantity.intValue())
                .filter(article -> maxQuantity == null || article.getQuantite() <= maxQuantity.intValue())
                .collect(Collectors.toList());
    }

    // Method for searching by description only
    public List<Article> searchByDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return findAll();
        }
        return findAll().stream()
                .filter(article -> article.getDescription().toLowerCase().contains(description.toLowerCase()))
                .collect(Collectors.toList());
    }
}
