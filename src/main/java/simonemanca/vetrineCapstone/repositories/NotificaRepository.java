package simonemanca.vetrineCapstone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simonemanca.vetrineCapstone.entities.Notifica;

import java.util.List;

@Repository
public interface NotificaRepository extends JpaRepository<Notifica, Integer> {
    List<Notifica> findByReadFalse();
    List<Notifica> findByTipo(String tipo);
}



