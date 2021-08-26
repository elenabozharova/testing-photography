package mk.ukim.finki.photography.web.controller;

import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.service.FollowService;
import mk.ukim.finki.photography.service.ImageService;
import mk.ukim.finki.photography.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final ImageService imageService;
    private final FollowService followService;
    private final PasswordEncoder passwordEncoder;

    public UsersController(UserService userService,
                           ImageService imageService, FollowService followService,
                           PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.imageService = imageService;
        this.followService = followService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showList(@RequestParam(required = false) String username,
                           Model model){
        List<User> users = this.userService.findAllByUsernameNameSurname(username);
        model.addAttribute("users", users);
        model.addAttribute("bodyContent", "users");
        return "master";
    }

    @Transactional
    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id, Model model, HttpServletResponse response, HttpServletRequest request){
        response.setContentType("image/*");
        User user = this.userService.findById(id);
        List<Image> photos = this.imageService.findAllByUser(id);
        List<User> followers = this.followService.getFollowers(user.getId());
        User loggedInUser = this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("loggedUser", loggedInUser);
        model.addAttribute("user", user);
        model.addAttribute("photos", photos);
        model.addAttribute("followers", followers);
        model.addAttribute("bodyContent", "user");
        return "master";
    }

    @Transactional
    @GetMapping("profile/{username}")
    public String getUserByUsername(@PathVariable String username,
                                    HttpServletResponse response,
                                    Model model,
                                    HttpServletRequest request){
        response.setContentType("image/*");
        User user = this.userService.findByUsername(username);
        Long id = user.getId();
        List<Image> photos = this.imageService.findAllByUser(id);
        List<User> followers = this.followService.getFollowers(user.getId());
        List<User> following = this.followService.getFollowingList(user.getId());
        User loggedInUser = this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("loggedUser", loggedInUser);
        model.addAttribute("user", user);
        model.addAttribute("photos", photos);
        model.addAttribute("followers", followers);
        model.addAttribute("following", following);
        model.addAttribute("bodyContent", "user");
        return "master";
    }


    @GetMapping("/edit/{id}")
    public String getEdit(@PathVariable Long id, Model model){
        User user = this.userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("bodyContent", "edituser");
        return "master";
    }


    @PostMapping("/edit/{id}")
    public String editProfile(@PathVariable Long id,
                              @RequestParam String username,
                              @RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam (required = false) MultipartFile profilePicture,
                              @RequestParam Role role,
                              HttpServletRequest request) throws IOException {
        User user = this.userService.findById(id);
        boolean result = true;
        String contentType= profilePicture.getContentType();
        if(!contentType.equals("image/png") && !contentType.equals("image/jpg") && !contentType.equals("image/jpeg")){
            this.userService.saveUserToDatabase(request, id, name, surname, role, request.getRemoteUser());
        }
        else{
            this.userService.saveUserToDatabase(request, id, name, surname, profilePicture, role, request.getRemoteUser());

        }
        return "redirect:/users/{id}";
    }


}
