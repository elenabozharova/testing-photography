package mk.ukim.finki.photography.repository;

import mk.ukim.finki.photography.model.Photoshoot;
import mk.ukim.finki.photography.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.social.facebook.api.Photo;

import java.util.List;

public interface PhotoshootRepository extends JpaRepository<Photoshoot, Long> {

    List<Photoshoot> findAllByPhotographer(User user);
}
