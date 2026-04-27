package marco.calculadora_obra.infrastructure.persistence.repository;

import marco.calculadora_obra.infrastructure.persistence.entity.ArestaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para ArestaEntity.
 * Princípio D: a camada de aplicação depende desta abstração de repositório.
 * Spring Data JPA gera todos os métodos CRUD automaticamente.
 */
@Repository
public interface ArestaRepository extends JpaRepository<ArestaEntity, Long> {
}
