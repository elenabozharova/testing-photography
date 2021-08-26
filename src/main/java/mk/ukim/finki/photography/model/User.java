package mk.ukim.finki.photography.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;

@Entity
@Table(name = "photography_users")
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String name;

    private String surname;

    @Enumerated(EnumType.STRING)
    private Authentication authProvider;

    @Lob
    @Column(length = 1000000000)
    private String imageSrc;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<Image> images;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Comment> comments;

    private String linkedin;

    private String facebook;

    private String instagram;

    @NotNull
    private String email;

    @ManyToMany
    @JoinTable(name="likes",
                joinColumns=
                @JoinColumn(name="user_id", referencedColumnName = "id"),
                inverseJoinColumns =
                @JoinColumn(name="image_id", referencedColumnName ="id"))
    private Set<Image> likes;

    @ManyToMany
    @JoinTable(name="follow",
                joinColumns=
                @JoinColumn(name="follower_id", referencedColumnName="id"),
                inverseJoinColumns =
                @JoinColumn(name="followed_id", referencedColumnName = "id"))
    private Set<User> followsList;

    @ManyToMany
    @JoinTable(name="follow",
            joinColumns =
            @JoinColumn(name="followed_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name="follower_id", referencedColumnName = "id"))
    private Set<User> followerList;

    public User(String name, String surname, String username, String password) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.comments = new HashSet<Comment>();
        this.likes = new HashSet<>();
        this.followsList = new HashSet<>();
        this.followerList = new HashSet<>();
    }

    public User(String username, String password, String name, String surname,  Role role, String linkedin, String instagram, String facebook) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.comments = new HashSet<Comment>();
        this.likes = new HashSet<>();
        this.followsList = new HashSet<>();
        this.followerList = new HashSet<>();
        this.role = role;
        this.linkedin = linkedin;
        this.instagram = instagram;
        this.facebook = facebook;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Set<Image> getImages() {
        return images;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Set<Image> getLikes() {
        return likes;
    }
    @Transactional
    public Set<User> getFollowsList() {
        return followsList;
    }

    @Lob
    @Transactional
    public Set<User> getFollowerList() {
        return followerList;
    }

    public Role getRole() {
        return role;
    }

    public Authentication getAuthProvider() {
        return authProvider;
    }


    @Transactional
    public String getImageSrc() {
        return imageSrc;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void setLikes(Set<Image> likes) {
        this.likes = likes;
    }

    public void setFollowsList(Set<User> followsList) {
        this.followsList = followsList;
    }
    @Transactional
    public void setFollowerList(Set<User> followerList) {
        this.followerList = followerList;
    }

    public void setAuthProvider(Authentication authProvider) {
        this.authProvider = authProvider;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
