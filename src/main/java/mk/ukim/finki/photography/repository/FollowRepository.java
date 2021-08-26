package mk.ukim.finki.photography.repository;

import mk.ukim.finki.photography.model.Comment;
import mk.ukim.finki.photography.model.Follow;
import mk.ukim.finki.photography.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findAllByFollowed(User user);
    List<Follow> findAllByFollower(User user);
    Follow findByFollowerAndFollowed(User follower, User followed);
}
