package marco.calculadora_obra.infrastructure.persistence.repository;

import marco.calculadora_obra.infrastructure.persistence.entity.VerticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerticeRepository extends JpaRepository<VerticeEntity, Long> {
}