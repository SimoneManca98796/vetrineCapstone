package simonemanca.vetrineCapstone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import simonemanca.vetrineCapstone.entities.Notifica;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificaRepository extends JpaRepository<Notifica, Integer> {
    List<Notifica> findByTipo(String tipo);

    List<Notifica> findByUserId(UUID userId);

    @Query("SELECT n FROM Notifica n WHERE :userId NOT MEMBER OF n.readers")
    List<Notifica> findUnreadNotificationsByUserId(@Param("userId") UUID userId);

    @Query("SELECT n FROM Notifica n WHERE n.userId != :userId AND :userId NOT MEMBER OF n.readers")
    List<Notifica> findUnreadNotificationsByOtherUsers(@Param("userId") UUID userId);

    @Query("SELECT n FROM Notifica n WHERE n.userId != :currentUserId AND n.userId = :userId AND :currentUserId NOT MEMBER OF n.readers")
    List<Notifica> findUnreadNotificationsBySpecificUserAndNotReadByCurrentUser(@Param("userId") UUID userId, @Param("currentUserId") UUID currentUserId);

    @Query("SELECT n FROM Notifica n WHERE :userId NOT MEMBER OF n.readers AND n.userId <> :userId")
    List<Notifica> findUnreadByUserId(@Param("userId") UUID userId);

}
















