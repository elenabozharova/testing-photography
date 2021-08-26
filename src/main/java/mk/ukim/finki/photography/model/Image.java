package mk.ukim.finki.photography.model;

import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Image implements Comparable<Image>{
    @Id
    @GeneratedValue
    private Long id;

    //TODO - da se smeni da e Byte []
    @Lob
    @Column(length = 1000000000)
    private String imageSrc;

    private String imgName;

    private String description;

    private LocalDate date;

    @ManyToOne()
    @JoinColumn(name="photography_users_id")
    private User user;

    @OneToMany(mappedBy = "image", cascade = CascadeType.PERSIST)
    private Set<Comment> comments;

    @ManyToMany
    @JoinTable(name="likes",
               joinColumns =
                @JoinColumn(name="image_id", referencedColumnName = "id"),
                inverseJoinColumns =
                @JoinColumn(name="user_id", referencedColumnName = "id")
    )
    private Set<User> likes;

    public Image(String description,
                 String imageSrc,
                 String imgName,
                 User photographer
                 ){
        this.description = description;
        this.imageSrc = imageSrc;
        this.imgName = imgName;
        this.user = photographer;
        this.comments = new HashSet<>();
        this.likes = new HashSet<>();
    }
    public Image(String description,
                 String imageSrc,
                 String imgName,
                 User photographer,
                 LocalDate time
    ){
        this.description = description;
        this.imageSrc = imageSrc;
        this.imgName = imgName;
        this.user = photographer;
        this.comments = new HashSet<>();
        this.likes = new HashSet<>();
        this.date = time;
    }

    public Long getId() {
        return id;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public String getImgName() {
        return imgName;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }

    public void setDate(LocalDate time){
        this.date = time;
    }

    public Image(){

    }

    @Override
    public int compareTo(Image o) {
        if(this.getDate().compareTo(o.getDate())>0){
            // o is an older photo
            return -1;

        }
        else if(this.getDate().compareTo(o.getDate())<0){
            //o is a newer photo
            return 1;
        }
        return 0;
    }
}
