package simonemanca.vetrineCapstone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simonemanca.vetrineCapstone.entities.Azienda;

@Repository
public interface AziendaRepository extends JpaRepository<Azienda, Long> {
}

