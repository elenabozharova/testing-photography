package mk.ukim.finki.photography.repository;

import mk.ukim.finki.photography.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);
    public Optional<User> findByUsernameAndPassword(String username, String password);
    List<User> findAllByUsernameLike(String username);

    @Transactional
    @Query(value="select * from photography_users ul where ul.name like ?1 or ul.surname like ?1 or ul.username like ?1",
            nativeQuery = true)
    List<User> findAllByUsernameNameSurname(String keyword);


}
