package mk.ukim.finki.photography.web.controller;

import mk.ukim.finki.photography.model.Follow;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.service.FollowService;
import mk.ukim.finki.photography.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Controller
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;
    private final UserService service;

    public FollowController(FollowService followService, UserService service) {
        this.followService = followService;
        this.service = service;
    }
    @Transactional
    @PostMapping("/{id}")
    public String followUser(HttpServletRequest request,
                             @PathVariable Long id){
        String username = request.getRemoteUser();
        User user = this.service.findByUsername(username);
        User userToFollow = this.service.findById(id);
        Follow follow = this.followService.follow(user.getId(), userToFollow.getId());
        return "redirect:/users/" + userToFollow.getId();
    }

    @Transactional
    @PostMapping("/unflw/{id}")
    public String unfollowUser(HttpServletRequest request,
                               @PathVariable Long id){
        String username = request.getRemoteUser();
        User user = this.service.findByUsername(username);
        User userToFollow = this.service.findById(id);
        this.followService.unfollow(user.getId(), userToFollow.getId());
        return "redirect:/users/" + userToFollow.getId();
    }
}
