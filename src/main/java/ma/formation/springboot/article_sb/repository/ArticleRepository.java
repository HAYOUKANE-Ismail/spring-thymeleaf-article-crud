package ma.formation.springboot.article_sb.repository;

import ma.formation.springboot.article_sb.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // Find articles by description containing keyword (case insensitive)
    List<Article> findByDescriptionContainingIgnoreCase(String description);

    // Find articles by price range
    List<Article> findByPriceBetween(Double minPrice, Double maxPrice);

    // Find articles by quantite range (using the correct property name)
    List<Article> findByQuantiteBetween(Integer minQuantity, Integer maxQuantity);

    // Custom query for advanced search
    @Query("SELECT a FROM Article a WHERE " +
            "(:description IS NULL OR LOWER(a.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
            "(:minPrice IS NULL OR a.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR a.price <= :maxPrice) AND " +
            "(:minQuantity IS NULL OR a.quantite >= :minQuantity) AND " +
            "(:maxQuantity IS NULL OR a.quantite <= :maxQuantity)")
    List<Article> findArticlesByCriteria(@Param("description") String description,
                                         @Param("minPrice") Double minPrice,
                                         @Param("maxPrice") Double maxPrice,
                                         @Param("minQuantity") Integer minQuantity,
                                         @Param("maxQuantity") Integer maxQuantity);
}
