package ma.formation.springboot.article_sb.controller;

import lombok.RequiredArgsConstructor;
import ma.formation.springboot.article_sb.model.Article;
import ma.formation.springboot.article_sb.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ArticleService articleService;

    // Dashboard page - NOW THE ONLY /dashboard mapping
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            List<Article> articles = articleService.findAll();
            model.addAttribute("totalArticles", articles.size());

            // Calculate statistics for the model (optional - mainly using AJAX)
            long inStock = articles.stream().filter(a -> a.getQuantite() > 0).count();
            long lowStock = articles.stream().filter(a -> a.getQuantite() > 0 && a.getQuantite() <= 10).count();
            double totalValue = articles.stream().mapToDouble(a -> a.getPrice() * a.getQuantite()).sum();

            model.addAttribute("inStockCount", inStock);
            model.addAttribute("lowStockCount", lowStock);
            model.addAttribute("totalValue", String.format("%.2f", totalValue));
            model.addAttribute("articles", articles);

            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            return "index";
        }
    }

    // Dashboard statistics API endpoint
    @GetMapping("/dashboard/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        try {
            List<Article> articles = articleService.findAll();

            Map<String, Object> stats = new HashMap<>();

            // Calculate real statistics from database
            int totalArticles = articles.size();
            long inStock = articles.stream().filter(a -> a.getQuantite() > 0).count();
            long lowStock = articles.stream().filter(a -> a.getQuantite() > 0 && a.getQuantite() <= 10).count();
            double totalValue = articles.stream().mapToDouble(a -> a.getPrice() * a.getQuantite()).sum();

            // Basic statistics
            stats.put("totalArticles", totalArticles);
            stats.put("inStock", inStock);
            stats.put("lowStock", lowStock);
            stats.put("totalValue", Math.round(totalValue * 100.0) / 100.0); // Round to 2 decimal places

            // Additional statistics for charts
            Map<String, Object> stockStatus = new HashMap<>();
            stockStatus.put("inStock", inStock);
            stockStatus.put("lowStock", lowStock);
            stockStatus.put("outOfStock", totalArticles - inStock);
            stats.put("stockStatus", stockStatus);

            // Sample inventory distribution (enhance based on your categories)
            Map<String, Object> inventoryDistribution = new HashMap<>();
            inventoryDistribution.put("labels", new String[]{"Electronics", "Clothing", "Books", "Home & Garden", "Sports"});
            inventoryDistribution.put("values", new int[]{35, 25, 20, 12, 8});
            stats.put("inventoryDistribution", inventoryDistribution);

            // Sample stock levels (enhance based on your categories)
            Map<String, Object> stockLevels = new HashMap<>();
            stockLevels.put("categories", new String[]{"Electronics", "Clothing", "Books", "Home & Garden", "Sports"});
            stockLevels.put("quantities", new int[]{45, 32, 28, 15, 12});
            stats.put("stockLevels", stockLevels);

            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            // Return default values if there's an error
            Map<String, Object> defaultStats = new HashMap<>();
            defaultStats.put("totalArticles", 0);
            defaultStats.put("inStock", 0);
            defaultStats.put("lowStock", 0);
            defaultStats.put("totalValue", 0.0);

            return ResponseEntity.ok(defaultStats);
        }
    }
}