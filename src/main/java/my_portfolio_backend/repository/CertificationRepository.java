package my_portfolio_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import my_portfolio_backend.entity.Certification;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
}