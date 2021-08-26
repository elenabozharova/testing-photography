package mk.ukim.finki.photography.repository;

import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Transactional
    @Query(value = "SELECT * FROM image i where i.photography_users_id = ?1", nativeQuery = true)
    List<Image> findAllByUserId(Long id);


}
