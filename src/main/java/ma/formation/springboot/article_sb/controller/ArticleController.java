package ma.formation.springboot.article_sb.controller;

import lombok.RequiredArgsConstructor;
import ma.formation.springboot.article_sb.model.Article;
import ma.formation.springboot.article_sb.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // Home/Welcome page
    @GetMapping("/")
    public String welcome(Model model) {
        List<Article> articles = articleService.findAll();
        // Limit to 5 recent articles for welcome page
        if (articles.size() > 5) {
            articles = articles.subList(0, 5);
        }
        model.addAttribute("articles", articles);
        return "index";
    }

    // Show all articles
    @GetMapping("/articles")
    public String listArticles(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "articles/list";
    }

    // Show create article form
    @GetMapping("/articles/create")
    public String showCreateForm(Model model) {
        model.addAttribute("article", new Article());
        return "articles/create";
    }

    // Handle create article form submission
    @PostMapping("/articles/create")
    public String createArticle(@ModelAttribute("article") Article article,
                                RedirectAttributes redirectAttributes) {
        try {
            articleService.create(article);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Article created successfully!");
            return "redirect:/articles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error creating article: " + e.getMessage());
            return "redirect:/articles/create";
        }
    }

    // Show edit article form
    @GetMapping("/articles/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Article article = articleService.findById(id);
            model.addAttribute("article", article);
            return "articles/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Article not found!");
            return "redirect:/articles";
        }
    }

    // Handle edit article form submission
    @PostMapping("/articles/edit/{id}")
    public String updateArticle(@PathVariable("id") Long id,
                                @ModelAttribute("article") Article article,
                                RedirectAttributes redirectAttributes) {
        try {
            articleService.update(id, article);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Article updated successfully!");
            return "redirect:/articles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error updating article: " + e.getMessage());
            return "redirect:/articles/edit/" + id;
        }
    }

    // Delete article
    @GetMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            articleService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Article deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting article: " + e.getMessage());
        }
        return "redirect:/articles";
    }

    // Show search form
    @GetMapping("/articles/search")
    public String showSearchForm(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "articles/search";
    }

    // Handle search form submission
    @PostMapping("/articles/search")
    public String searchArticles(@RequestParam(value = "description", required = false) String description,
                                 @RequestParam(value = "minPrice", required = false) Double minPrice,
                                 @RequestParam(value = "maxPrice", required = false) Double maxPrice,
                                 @RequestParam(value = "minQuantity", required = false) Double minQuantity,
                                 @RequestParam(value = "maxQuantity", required = false) Double maxQuantity,
                                 Model model) {
        try {
            List<Article> articles;

            // If only description is provided (from header search), use simple search
            if (description != null && !description.trim().isEmpty() &&
                    minPrice == null && maxPrice == null && minQuantity == null && maxQuantity == null) {
                articles = articleService.searchByDescription(description);
                model.addAttribute("articles", articles);
                model.addAttribute("searchDescription", description);
                return "articles/search-results";
            } else {
                // Advanced search
                articles = articleService.searchArticles(description, minPrice, maxPrice, minQuantity, maxQuantity);
                model.addAttribute("articles", articles);
                return "articles/search-results";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error searching articles: " + e.getMessage());
            return "articles/search";
        }
    }

    // REMOVED: Dashboard mapping - now handled by DashboardController
    // The dashboard functionality has been moved to DashboardController.java

    // Export PDF (placeholder)
    @GetMapping("/export/pdf")
    public String exportPdf(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("infoMessage",
                "PDF export feature will be implemented soon!");
        return "redirect:/articles";
    }
}