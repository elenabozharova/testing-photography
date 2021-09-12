package mk.ukim.finki.photography.comment;


import mk.ukim.finki.photography.model.Comment;
import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.model.exceptions.UserIdNotFoundException;
import mk.ukim.finki.photography.service.CommentService;
import mk.ukim.finki.photography.service.ImageService;
import mk.ukim.finki.photography.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentSeleniumTests {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    // test no comments
    @Test
    public void testNoComments()
    {
        // at this moment in the database there are no comments
        int numberOfComments = commentService.showAll().size();
        Assert.assertEquals(0, numberOfComments);
    }

    // test add a comment
    @Test
    public void addCommentOnPicture()
    {
        // find a photographer that has photos
        User photographer = userService.findByUsername("elena.bozarova");
        Image image = imageService.findAllByUser(photographer.getId()).stream().findFirst().orElseThrow(UserIdNotFoundException::new);
       // find another user
        User user = userService.findByUsername("user@test.com");
        commentService.addComment("Great photo!", user.getId(), image.getId());

        Comment comment = commentService.findTopByOrderByIdDesc();
        Assert.assertEquals("Great photo!" , comment.getText());
    }




}
