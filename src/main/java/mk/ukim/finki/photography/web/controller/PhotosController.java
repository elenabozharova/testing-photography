package mk.ukim.finki.photography.web.controller;

import mk.ukim.finki.photography.model.Comment;
import mk.ukim.finki.photography.model.Image;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.service.CommentService;
import mk.ukim.finki.photography.service.ImageService;
import mk.ukim.finki.photography.service.UserService;
import org.apache.coyote.Request;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

@Controller
@RequestMapping("/photos")
public class PhotosController {

    private final ImageService imageService;
    private final UserService userService;
    private final CommentService commentService;

    public PhotosController(ImageService imageService, UserService userService, CommentService commentService) {
        this.imageService = imageService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @Transactional
    @GetMapping
    public String showAll(HttpServletRequest request, HttpServletResponse response, Model model){
        response.setContentType("image/*");
        List<Image> images = this.imageService.list();
        List<Comment> comments = this.commentService.showAll();
        User user = this.userService.findByUsername(request.getRemoteUser());
        Collections.sort(images);
        model.addAttribute("loggedInUser", user);
        model.addAttribute("comments", comments);
        model.addAttribute("images",images);
        model.addAttribute("bodyContent","listall");
        return "master";
    }

    @GetMapping("/{id}")
    @Transactional
    public String showPhoto(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response, Model model){
        response.setContentType("image/*");
        Image image = this.imageService.findById(id);
        List<Comment> comments = this.commentService.showAll();
        User user = this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("loggedInUser", user);
        model.addAttribute("comments", comments);
        model.addAttribute("image", image);
        model.addAttribute("bodyContent","image");
        return "master";
    }

    @GetMapping("/upload")
    public String showAdd(Model model){
        model.addAttribute("bodyContent","upload");
        return "master";
    }

    @PostMapping
    public String add(
            @RequestParam String name,
            @RequestParam String source,
            @RequestParam String description
            ){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
             username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        //TODO da se napravi exception
        User user = this.userService.findByUsername(username);
      //  Image image = new Image(name, source, description, user);
        return "redirect:/photos";
    }

    @PostMapping("/upload")
    public String addImage(@RequestParam String name,
                           @RequestParam String description,
                           @RequestParam("file") MultipartFile file,
                           HttpServletRequest request
                           ) throws IOException, ParseException {
        LocalDate date = LocalDate.now();
        this.imageService.saveImagetoDB(request,file, name, description, date);
        return "redirect:/photos";
    }

    @PostMapping("/like/{id}")
    public String like(@PathVariable Long id,
                       @RequestParam Long userId){
        this.imageService.likeImage(id, userId);
        return "redirect:/photos";
    }

    @PostMapping("unlike/{id}")
    public String unlike(@PathVariable Long id,
                         @RequestParam Long userId){
        this.imageService.unlikeImage(id, userId);
        return "redirect:/photos";
    }

    @GetMapping("edit/{id}")
    public String showEdit(@PathVariable Long id,Model model){
        Image image = this.imageService.findById(id);
        model.addAttribute("image", image);
        model.addAttribute("bodyContent", "editimage" );
        return "master";
    }

    @PostMapping("edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam String description,
                       @RequestParam String name){
        this.imageService.editImage(id,description, name);
        return "redirect:/photos";
    }

    @PostMapping("delete/{id}")
    public String delete(@PathVariable Long id, HttpServletRequest request){
        Image image = this.imageService.findById(id);
        this.imageService.delete(image);
        return "redirect:/home";
    }
}
