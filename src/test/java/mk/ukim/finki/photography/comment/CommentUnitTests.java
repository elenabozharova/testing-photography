package mk.ukim.finki.photography.comment;

import mk.ukim.finki.photography.model.Comment;
import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.repository.CommentRepository;
import mk.ukim.finki.photography.repository.ImageRepository;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.impl.CommentServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CommentUnitTests {
    /**
     * This class is intended to rest the CommentService .
     * */

    @Mock
    public CommentRepository commentRepository;

    @Mock
    public UserRepository userRepository;

    @Mock
    public ImageRepository imageRepository;

    public CommentServiceImpl service;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);

        User user1 = new User("elena.user", "password", "name", "surname", Role.ROLE_USER, "","", "");
        user1.setId((long)976);

        User user2 = new User("elena.test", "password", "name", "surname", Role.ROLE_USER, "","", "");
        user2.setId((long)645);

        Image image1 = new Image("description", "src", "name", user1, LocalDate.now());
        image1.setId((long)465);

        Comment comment1 = new Comment("comment", user1, image1, LocalDate.now());
        Comment comment = new Comment("comment", user2, image1, LocalDate.now());

        List<Comment> comments = new ArrayList<Comment>();
        comments.add(comment1);
        comments.add(comment);

        Mockito.when(userRepository.findById((long)976)).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById((long)798)).thenReturn(Optional.empty());
        Mockito.when(imageRepository.findById((long)465)).thenReturn(Optional.of(image1));
        Mockito.when(imageRepository.findById((long)645)).thenReturn(Optional.empty());
        Mockito.when(commentRepository.findAll()).thenReturn(comments);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment1);

        service = Mockito.spy(new CommentServiceImpl(commentRepository, userRepository, imageRepository));
    }

    /**
     * In the init method, I make the comment repository have 2 comments
     * */

    @Test
    public void allCommentsTest()
    {

        Assert.assertEquals(2, service.showAll().size());
    }

    /**
     * Test the addComment function in the Comment Service
     * */
    @Test
    public void addCommentServiceUserNotFound()
    {
        Assert.assertThrows("NoSuchElement Exception expected", NoSuchElementException.class, ()-> service.addComment("comment", (long)798, (long)465));
    }

    @Test
    public void addCommentImageNotFound()
    {
        Assert.assertThrows("NOSuchElement Exception expected", NoSuchElementException.class, () -> service.addComment("comment", (long)976, (long)645));
    }

    @Test
    public void addComment()
    {
        Comment comment = service.addComment("content", (long)976, (long)465);
        Assert.assertEquals("content", comment.getText());
        Assert.assertEquals((long)976, (long)comment.getUser().getId());
        Assert.assertEquals((long)465, (long)comment.getImage().getId());
    }


}
