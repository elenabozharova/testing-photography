package mk.ukim.finki.photography.service;

import mk.ukim.finki.photography.model.Follow;
import mk.ukim.finki.photography.model.User;

import java.util.List;

public interface FollowService {
    public Follow follow(Long followerId, Long userFollowId);
    void unfollow(Long followerId, Long userFollowId);
    List<User> getFollowers(Long userId);
    List<User> getFollowingList(Long id);
}
