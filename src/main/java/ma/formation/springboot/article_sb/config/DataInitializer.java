package ma.formation.springboot.article_sb.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import ma.formation.springboot.article_sb.model.Article;
import ma.formation.springboot.article_sb.model.User;
import ma.formation.springboot.article_sb.repository.ArticleRepository;
import ma.formation.springboot.article_sb.repository.UserRepository;
import ma.formation.springboot.article_sb.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void init() {
        // Initialize admin user
        if (userRepository.findByUsername("admin").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("admin");
            user.setRole("ADMIN");
            userService.register(user);
        }

        // Truncate the articles table to reset AUTO_INCREMENT
        jdbcTemplate.execute("TRUNCATE TABLE articles");

        // Insert sample articles
        articleRepository.saveAll(List.of(
                new Article("Chaise en bois", 10, 35.50),
                new Article("Table en verre", 15, 120.75),
                new Article("Lampe de bureau", 20, 45.00),
                new Article("Fauteuil en cuir", 8, 150.90),
                new Article("Ordinateur portable", 12, 899.99),
                new Article("Tapis de salon", 25, 60.50),
                new Article("Chaise de bureau ergonomique", 30, 75.20),
                new Article("Étagère en métal", 18, 55.60),
                new Article("Projecteur de cinéma", 5, 220.00),
                new Article("Télévision LED 55\"", 10, 450.00),
                new Article("Coffre-fort électronique", 7, 350.30),
                new Article("Clé USB 64 Go", 50, 15.20),
                new Article("Imprimante laser", 13, 200.00),
                new Article("Station de charge pour téléphone", 40, 25.75),
                new Article("Montre connectée", 22, 120.50),
                new Article("Casque audio sans fil", 18, 80.00),
                new Article("Support pour ordinateur portable", 35, 30.90),
                new Article("Lampe de chevet LED", 28, 19.99),
                new Article("Cadenas à combinaison", 50, 10.00),
                new Article("Aspirateur robot", 6, 280.00)
        ));

        System.out.println("Sample articles initialized with IDs starting from 1");
    }
}