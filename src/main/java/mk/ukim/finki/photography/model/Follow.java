package mk.ukim.finki.photography.model;

import javax.persistence.*;

@Entity
public class Follow {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne()
    @JoinColumn(name="followed_id")
    private User followed;

    @ManyToOne()
    @JoinColumn(name="follower_id")
    private User follower;

    public Follow(){

    }

    public Long getId() {
        return id;
    }

    public User getFollowed() {
        return followed;
    }

    public User getFollower() {
        return follower;
    }

    public Follow(User follower, User followed){
        this.follower = follower;
        this.followed = followed;
    }
}