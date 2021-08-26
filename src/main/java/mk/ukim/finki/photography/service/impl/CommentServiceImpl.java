package mk.ukim.finki.photography.service.impl;

import mk.ukim.finki.photography.model.Comment;
import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.repository.CommentRepository;
import mk.ukim.finki.photography.repository.ImageRepository;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final UserRepository userRepo;
    private final ImageRepository imageRepository;

    public CommentServiceImpl(CommentRepository repository, UserRepository userRepo, ImageRepository imageRepository) {
        this.repository = repository;
        this.userRepo = userRepo;
        this.imageRepository = imageRepository;
    }

    @Override
    public List<Comment> showAll() {
        return this.repository.findAll();
    }


    @Override
    public Comment addComment(String comment, Long userId, Long imageId) {
        User user = this.userRepo.findById(userId).orElseThrow();
        Image image = this.imageRepository.findById(imageId).orElseThrow();
        Comment tmp = new Comment(comment, user, image);
        return this.repository.save(tmp);
    }
}
