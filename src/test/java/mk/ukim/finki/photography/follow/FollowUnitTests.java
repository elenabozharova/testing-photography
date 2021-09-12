package mk.ukim.finki.photography.follow;

import mk.ukim.finki.photography.model.Follow;
import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.model.exceptions.UsernameNotFoundException;
import mk.ukim.finki.photography.repository.FollowRepository;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.FollowService;
import mk.ukim.finki.photography.service.UserService;
import mk.ukim.finki.photography.service.impl.FollowServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.*;

import static org.aspectj.bridge.MessageUtil.fail;

@RunWith(MockitoJUnitRunner.class)
public class FollowUnitTests {
/**
 *  Tests for the service FollowService class.
 *  The repositories are used with @Mock annotation to test only teh service methods.
 * */

    @Mock
    public UserRepository userRepository;

    @Mock
    public FollowRepository followRepository;

    private FollowServiceImpl followService;

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        User user1 = new User("elena.user", "password", "name", "surname", Role.ROLE_USER, "","", "");
        user1.setId((long)976);

        User user2 = new User("elena.username", "password", "name", "surname", Role.ROLE_USER, "","", "");
        user2.setId((long)798);

        User user3 = new User("elena.username", "password", "name", "surname", Role.ROLE_USER, "","", "");
        user3.setId((long)675);

        Follow followSuccessful = new Follow(user1, user2);

        List<Follow> followers = new ArrayList<Follow>();
        followers.add(new Follow(user1, user2));

        List<Follow> following = new ArrayList<Follow>();
        following.add(new Follow(user1, user2));

        Mockito.when(this.userRepository.findByUsername("elena.user")).thenReturn(Optional.of(user1));
        Mockito.when(this.userRepository.findByUsername("elena.username")).thenReturn(Optional.of(user2));
        Mockito.when(this.userRepository.findById((long)976)).thenReturn(Optional.of(user1));
        Mockito.when(this.userRepository.findById((long)798)).thenReturn(Optional.of(user2));
        Mockito.when(this.userRepository.findById((long)654)).thenReturn(Optional.empty());
        Mockito.when(this.userRepository.findById((long)658)).thenReturn(Optional.empty());
        Mockito.when(this.followRepository.save(Mockito.any(Follow.class))).thenReturn(followSuccessful);
        Mockito.when(this.followRepository.findAllByFollowed(user2)).thenReturn(followers);
        Mockito.when(this.followRepository.findAllByFollower(user1)).thenReturn(following);
        followService = Mockito.spy(new FollowServiceImpl(userRepository, followRepository));
    }

    /**
     * Tests for the follow method using graphs, edge criterion.
     * */

    @Test
    public void testFollowFollowerNotFound()
    {
        Assert.assertThrows("NoSuchElement exception expected", NoSuchElementException.class, ()-> this.followService.follow((long)654, (long)976));
    }

    @Test
    public void testFollowFollowingNotFound()
    {
        Assert.assertThrows("NoSuchElement exception expected", NoSuchElementException.class, () -> this.followService.follow((long)976, (long)654));
    }

    @Test
    public void testFollow()
    {
         Follow follow = followService.follow((long)976, (long)798);
         User follower = userRepository.findById((long)976).get();
         User following = userRepository.findById((long)798).get();
         Assert.assertEquals((long)follow.getFollower().getId(), (long)976);
    }

    @Test
    public void testUnfollowFollowerNotFound()
    {
        Assert.assertThrows("UsernameNotFoundException expected", UsernameNotFoundException.class, ()-> this.followService.unfollow((long)654, (long)976));
    }

    @Test
    public void testUnfollowFollowedNotFound()
    {
        Assert.assertThrows("UsernameNotFoundException should be thrown", UsernameNotFoundException.class, () -> this.followService.unfollow((long)976, (long)654));
    }

    /** Could not be tested - the return type was void , just check if there was an exception thrown*/
    @Test
    public void testUnfollow()
    {
        User user1 = this.userRepository.findById((long)976).get();
        User user2 = this.userRepository.findById((long)798).get();
        // follow the user 2
        this.followService.follow(user1.getId(), user2.getId());
        // unfollow the user 2
        this.followService.unfollow(user1.getId(), user2.getId());
        Assert.assertEquals(0, user2.getFollowerList().size());
    }

    // test the get FollowersList functionality
    @Test
    public void getFollowersListUserNotFound()
    {
        Assert.assertThrows("NoSuchElementException", NoSuchElementException.class, ()-> followService.getFollowers((long)654));
    }

    /**
     * Testing the getFollowers list functionality.
     * The user I send in the method, has one follower user1.
     * */

    @Test
    public void getFollowersList()
    {
        List<User> followers = this.followService.getFollowers((long)798);
        User follower1 = followers.get(0);
        Assert.assertEquals(1, followers.size());
        Assert.assertEquals((long)976, (long)follower1.getId());
    }


    /**
     * Testing the getFollowing method.
     * The user1, follows user2.
     * */

    @Test
    public void getFollowing()
    {
        List<User> following = this.followService.getFollowingList((long)976);
        User following1 = following.get(0);
        Assert.assertEquals((long)798, (long) following1.getId());
    }



}
