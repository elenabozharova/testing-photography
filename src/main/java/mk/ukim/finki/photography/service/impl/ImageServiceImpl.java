package mk.ukim.finki.photography.service.impl;

import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.model.exceptions.UsernameNotFoundException;
import mk.ukim.finki.photography.repository.ImageRepository;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository repository;
    private final UserRepository userRepos;

    public ImageServiceImpl(ImageRepository repository, UserRepository userRepos) {
        this.repository = repository;
        this.userRepos = userRepos;
    }

    @Override
    public Image save(Image image) {
        return this.repository.save(image);
    }

    @Transactional
    public void saveImagetoDB(HttpServletRequest request, MultipartFile file, String name, String des) throws IOException {
        String fileName = file.getOriginalFilename();
        String encodedImage = Base64.getEncoder().encodeToString(file.getBytes());
        String authorUsername = request.getRemoteUser().toString();
        User author = this.userRepos.findByUsername(authorUsername).orElseThrow(() -> new UsernameNotFoundException(authorUsername));
        Image image = new Image(des, encodedImage, fileName, author);
        this.save(image);
    }


    @Override
    @Transactional
    public void saveImagetoDB(HttpServletRequest request, MultipartFile file, String name, String des, LocalDate time) throws IOException {
        String fileName = file.getOriginalFilename();
        String encodedImage = Base64.getEncoder().encodeToString(file.getBytes());
        String authorUsername = request.getRemoteUser().toString();
        User author = this.userRepos.findByUsername(authorUsername).orElseThrow(() -> new UsernameNotFoundException(authorUsername));
        Image image = new Image(des, encodedImage, fileName, author, time);
        this.save(image);
    }

    @Override
    public List<Image> list() {
        return this.repository.findAll();
    }

    @Override
    public List<Image> findAllByUser(Long id) {
        return this.repository.findAllByUserId(id);
    }

    @Transactional
    @Override
    public Image likeImage(Long image, Long user){
        Image image1 = this.repository.findById(image).orElseThrow();
        User user1 = this.userRepos.findById(user).orElseThrow();
        user1.getLikes().add(image1);
        this.userRepos.save(user1);
        return image1;
    }

    @Transactional
    @Override
    public Image unlikeImage(Long image, Long user) {
        Image image1 = this.repository.findById(image).orElseThrow();
        User user1 = this.userRepos.findById(user).orElseThrow();
        user1.getLikes().remove(image1);
        this.userRepos.save(user1);
        return image1;
    }

    @Override
    public Image findById(Long id) {
        return this.repository.findById(id).orElseThrow();
    }

    @Override
    public Image editImage(Long id, String description, String name) {
        Image image = this.repository.findById(id).orElseThrow();
        image.setDescription(description);
        image.setImgName(name);
        return this.repository.save(image);
    }

    @Override
    public void delete(Image image) {
        this.repository.delete(image);
    }
}
