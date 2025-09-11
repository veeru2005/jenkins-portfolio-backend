package my_portfolio_backend.controller;

import my_portfolio_backend.entity.Certification;
import my_portfolio_backend.entity.Project;
import my_portfolio_backend.service.CloudinaryService;
import my_portfolio_backend.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioContentController {

    private final PortfolioService portfolioService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public PortfolioContentController(PortfolioService portfolioService, CloudinaryService cloudinaryService) {
        this.portfolioService = portfolioService;
        this.cloudinaryService = cloudinaryService;
    }

    // ===========================
    // PROJECTS
    // ===========================
    @GetMapping("/projects")
    public List<Project> getProjects() {
        return portfolioService.getAllProjects();
    }

    @PostMapping(value = "/admin/projects", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveProject(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("technologies") String technologies,
            @RequestParam("liveUrl") String liveUrl,
            @RequestParam("githubUrl") String githubUrl,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        try {
            if (title == null || title.isBlank()) return ResponseEntity.badRequest().body("Project title is required");

            String imageUrl = (imageFile != null && !imageFile.isEmpty())
                    ? cloudinaryService.upload(imageFile)
                    : "/images/placeholder.svg";

            Project project = new Project(title, description, imageUrl, technologies, liveUrl, githubUrl);
            Project saved = portfolioService.saveProject(project);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving project: " + e.getMessage());
        }
    }

    @PutMapping(value = "/admin/projects/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateProject(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("technologies") String technologies,
            @RequestParam("liveUrl") String liveUrl,
            @RequestParam("githubUrl") String githubUrl,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        try {
            Project existing = portfolioService.getAllProjects()
                    .stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Project not found"));

            String imageUrl = (imageFile != null && !imageFile.isEmpty())
                    ? cloudinaryService.upload(imageFile)
                    : existing.getImageUrl(); // preserve existing if no new image

            existing.setTitle(title);
            existing.setDescription(description);
            existing.setTechnologies(technologies);
            existing.setLiveUrl(liveUrl);
            existing.setGithubUrl(githubUrl);
            existing.setImageUrl(imageUrl);

            Project saved = portfolioService.updateProject(id, existing);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating project: " + e.getMessage());
        }
    }

    @DeleteMapping("/admin/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        portfolioService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // ===========================
    // CERTIFICATIONS
    // ===========================
    @GetMapping("/certifications")
    public List<Certification> getCertifications() {
        return portfolioService.getAllCertifications();
    }

    @PostMapping(value = "/admin/certifications", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveCertification(
            @RequestParam("name") String name,
            @RequestParam("issuer") String issuer,
            @RequestParam("year") String year,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        try {
            if (name == null || name.isBlank()) return ResponseEntity.badRequest().body("Certification name is required");

            String imageUrl = (imageFile != null && !imageFile.isEmpty())
                    ? cloudinaryService.upload(imageFile)
                    : "/images/placeholder.svg";

            Certification cert = new Certification(name, issuer, year, description, imageUrl);
            Certification saved = portfolioService.saveCertification(cert);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving certification: " + e.getMessage());
        }
    }

    @PutMapping(value = "/admin/certifications/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateCertification(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("issuer") String issuer,
            @RequestParam("year") String year,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        try {
            Certification existing = portfolioService.getAllCertifications()
                    .stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Certification not found"));

            String imageUrl = (imageFile != null && !imageFile.isEmpty())
                    ? cloudinaryService.upload(imageFile)
                    : existing.getImageUrl(); // preserve existing if no new image

            existing.setName(name);
            existing.setIssuer(issuer);
            existing.setYear(year);
            existing.setDescription(description);
            existing.setImageUrl(imageUrl);

            Certification saved = portfolioService.updateCertification(id, existing);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating certification: " + e.getMessage());
        }
    }

    @DeleteMapping("/admin/certifications/{id}")
    public ResponseEntity<Void> deleteCertification(@PathVariable Long id) {
        portfolioService.deleteCertification(id);
        return ResponseEntity.noContent().build();
    }
}
