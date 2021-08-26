package mk.ukim.finki.photography.repository;

import mk.ukim.finki.photography.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long > {
}
