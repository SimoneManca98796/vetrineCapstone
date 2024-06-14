package simonemanca.vetrineCapstone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import simonemanca.vetrineCapstone.entities.Notifica;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificaRepository extends JpaRepository<Notifica, Integer> {
    List<Notifica> findByReadFalse();
    List<Notifica> findByTipo(String tipo);
    List<Notifica> findByUserId(UUID userId);
    List<Notifica> findByUserIdAndReadFalse(UUID userId);
    List<Notifica> findByUserIdNotAndReadFalse(UUID userId);
    List<Notifica> findByUserIdNotAndReadFalseAndUserIdNot(UUID userId, UUID currentUserId);
}














