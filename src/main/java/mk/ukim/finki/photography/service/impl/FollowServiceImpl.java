package mk.ukim.finki.photography.service.impl;

import mk.ukim.finki.photography.model.Follow;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.model.exceptions.UsernameNotFoundException;
import mk.ukim.finki.photography.repository.FollowRepository;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.FollowService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final FollowRepository repository;

    public FollowServiceImpl(UserRepository userRepository, FollowRepository repository) {
        this.userRepository = userRepository;
        this.repository = repository;
    }

    @Override
    public Follow follow(Long followerId, Long followedId) {
        Follow follow;
        User loggedInUser = this.userRepository.findById(followerId).orElseThrow();
        User userToFollow = this.userRepository.findById(followedId).orElseThrow();
        follow = new Follow(loggedInUser, userToFollow);
        return this.repository.save(follow);
    }

    @Transactional
    @Override
    public void unfollow(Long followerId, Long userFollowId) {
        Follow follow;
        User follower = this.userRepository.findById(followerId).orElseThrow(()-> new UsernameNotFoundException(""));
        User followed = this.userRepository.findById(userFollowId).orElseThrow(()-> new UsernameNotFoundException(""));
        follow = this.repository.findByFollowerAndFollowed(follower, followed);
        this.repository.delete(follow);
    }

    @Transactional
    @Override
    public List<User> getFollowers(Long userId) {
        User user = this.userRepository.findById(userId).orElseThrow();
        List<Follow> followersData = this.repository.findAllByFollowed(user);
        List<User> followers = new LinkedList<>();
        for (Follow follower : followersData){
            followers.add(follower.getFollower());
        }
        return followers;
    }

    @Override
    @Transactional
    public List<User> getFollowingList(Long id) {
        User user = this.userRepository.findById(id).orElseThrow();
        List<Follow> followersData = this.repository.findAllByFollower(user);
        List<User> following = new LinkedList<>();
        for (Follow follower : followersData){
            following.add(follower.getFollowed());
        }
        return following;
    }
}
