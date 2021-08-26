package mk.ukim.finki.photography.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne()
    @JoinColumn(name="photography_users_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name="image_id")
    private Image image;

    private String text;

    private LocalDate time;

    public Long getId() {
        return id;
    }

    public Comment(String text, User user, Image image){
        this.text = text;
        this.user = user;
        this.image = image;
    }

    public Comment(String text, User user, Image image, LocalDate date){
        this.text = text;
        this.user = user;
        this.image = image;
        this.time = date;
    }

    Comment(){

    }

    public User getUser() {
        return user;
    }

    public Image getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setText(String text) {
        this.text = text;
    }



}
