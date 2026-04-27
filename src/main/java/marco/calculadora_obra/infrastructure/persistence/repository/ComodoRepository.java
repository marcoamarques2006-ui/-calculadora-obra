package marco.calculadora_obra.infrastructure.persistence.repository;

import marco.calculadora_obra.infrastructure.persistence.entity.ComodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComodoRepository extends JpaRepository<ComodoEntity, Long> {
}