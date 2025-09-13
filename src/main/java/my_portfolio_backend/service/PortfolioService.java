package my_portfolio_backend.service;

import my_portfolio_backend.entity.Certification;
import my_portfolio_backend.entity.ContactMessage;
import my_portfolio_backend.entity.Project;
import my_portfolio_backend.repository.CertificationRepository;
import my_portfolio_backend.repository.ContactMessageRepository;
import my_portfolio_backend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioService {

    private final ContactMessageRepository contactMessageRepository;
    private final ProjectRepository projectRepository;
    private final CertificationRepository certificationRepository;
    // The email service is no longer needed for this method, but other methods might use it.
    // We will leave it for now but remove its use from saveContactMessage.
    private final Emai_Otp_Service emailService;

    @Autowired
    public PortfolioService(ContactMessageRepository contactMessageRepository, ProjectRepository projectRepository, CertificationRepository certificationRepository, Emai_Otp_Service emailService) {
        this.contactMessageRepository = contactMessageRepository;
        this.projectRepository = projectRepository;
        this.certificationRepository = certificationRepository;
        this.emailService = emailService;
    }

    // --- Contact Messages ---
    public void saveContactMessage(ContactMessage message) {
        // Step 1: Save the message (This is correct)
        contactMessageRepository.save(message);

        // Step 2: DELETE THE FOLLOWING LINES.
        // Your ContactController already sends the better-looking email.
        /*
        String notification = String.format("New message from: %s (%s)\n\nSubject: %s\n\nMessage:\n%s",
                message.getName(), message.getEmail(), message.getSubject(), message.getMessage());
        emailService.sendEmail("sunkavalli.veerendra1973@gmail.com", "New Portfolio Contact Message", notification);
        */
    }

    // --- Projects ---
    public List<Project> getAllProjects() { return projectRepository.findAll(); }
    public Project saveProject(Project project) { return projectRepository.save(project); }
    public Project updateProject(Long id, Project updated) {
        return projectRepository.findById(id).map(existing -> {
            existing.setTitle(updated.getTitle());
            existing.setDescription(updated.getDescription());
            if (updated.getImageUrl() != null) existing.setImageUrl(updated.getImageUrl());
            existing.setTechnologies(updated.getTechnologies());
            existing.setLiveUrl(updated.getLiveUrl());
            existing.setGithubUrl(updated.getGithubUrl());
            return projectRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Project with id " + id + " not found"));
    }
    public void deleteProject(Long id) { projectRepository.deleteById(id); }

    // --- Certifications ---
    public List<Certification> getAllCertifications() { return certificationRepository.findAll(); }
    public Certification saveCertification(Certification cert) { return certificationRepository.save(cert); }
    public Certification updateCertification(Long id, Certification updated) {
        return certificationRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setIssuer(updated.getIssuer());
            existing.setYear(updated.getYear());
            existing.setDescription(updated.getDescription());
            if (updated.getImageUrl() != null) existing.setImageUrl(updated.getImageUrl());
            return certificationRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Certification with id " + id + " not found"));
    }
    public void deleteCertification(Long id) { certificationRepository.deleteById(id); }
}