package mk.ukim.finki.photography.model;

import org.springframework.social.facebook.api.Photo;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Photoshoot {

    @Id
    @GeneratedValue
    private Long Id;

    @ManyToOne
    private User photographer;

    @ManyToOne
    private User user;

    private String date;

    private String hour;

    private String partOfDay;

    public Photoshoot(User photographer, User user, String date, String hour, String partOfDay) {
        this.photographer = photographer;
        this.user = user;
        this.date = date;
        this.partOfDay = partOfDay;
        this.hour = hour;
    }

    public Long getId() {
        return Id;
    }

    public User getPhotographer() {
        return photographer;
    }

    public User getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }


    public void setId(Long id) {
        Id = id;
    }

    public void setPhotographer(User photographer) {
        this.photographer = photographer;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public String getPartOfDay() {
        return partOfDay;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setPartOfDay(String partOfDay) {
        this.partOfDay = partOfDay;
    }

    public Photoshoot(){

    }

}
