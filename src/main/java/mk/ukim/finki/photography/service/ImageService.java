package mk.ukim.finki.photography.service;

import mk.ukim.finki.photography.model.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public interface ImageService {

    Image save(Image image);
    void saveImagetoDB(HttpServletRequest request, MultipartFile file, String name, String des, LocalDate date) throws IOException;
    List<Image> list();
    @Transactional
    List<Image> findAllByUser(Long id);

    Image likeImage(Long image, Long user);
    @Transactional
    Image unlikeImage(Long image, Long user);

    Image findById(Long id);

    Image editImage(Long id, String description, String name);

    void delete(Image image);
}
