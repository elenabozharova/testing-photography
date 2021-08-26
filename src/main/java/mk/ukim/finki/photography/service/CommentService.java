package mk.ukim.finki.photography.service;

import mk.ukim.finki.photography.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> showAll();
    Comment addComment(String comment, Long userId, Long imageId);
}
