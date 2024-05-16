package simonemanca.vetrineCapstone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import simonemanca.vetrineCapstone.entities.PrezziBovini;
import simonemanca.vetrineCapstone.entities.PrezziOvini;
import simonemanca.vetrineCapstone.entities.PrezzoStorico;

import java.util.Date;
import java.util.List;

public interface PrezziBoviniRepository extends JpaRepository<PrezziBovini, Long> {
    // Utilizza la convenzione di denominazione per definire la query
    Page<PrezziBovini> findByDataAndLuogo(Date data, String luogo, Pageable pageable);

    // Per query più complesse, utilizzerò l'annotazione @Query
    @Query("SELECT p FROM PrezziOvini p WHERE (:data IS NULL OR p.data = :data) AND (:luogo IS NULL OR p.luogo = :luogo OR :luogo = '')")
    List<PrezziBovini> findByDataAndLuogo(Date data, String luogo);
}