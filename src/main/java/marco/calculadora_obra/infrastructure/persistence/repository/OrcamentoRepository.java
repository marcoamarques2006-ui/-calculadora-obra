package marco.calculadora_obra.infrastructure.persistence.repository;

import marco.calculadora_obra.infrastructure.persistence.entity.OrcamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrcamentoRepository extends JpaRepository<OrcamentoEntity, Long> {

    /** Busca orçamentos pelo nome do usuário (parcial, ignorando maiúsculas/minúsculas). */
    List<OrcamentoEntity> findByNomeUsuarioContainingIgnoreCaseOrderByDataCriacaoDesc(String nomeUsuario);
}
