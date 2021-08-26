package mk.ukim.finki.photography.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mk.ukim.finki.photography.model.Photoshoot;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.service.PhotoshootService;
import mk.ukim.finki.photography.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("calendar")
public class PhotoshootController {
    private final UserService userService;
    private final PhotoshootService photoshootService;

    public PhotoshootController(UserService userService, PhotoshootService photoshootService) {
        this.userService = userService;
        this.photoshootService = photoshootService;
    }

    @GetMapping("/{id}")
    public String getCalendar(@PathVariable Long id, HttpServletRequest request, Model model) {
        User user = this.userService.findByUsername(request.getRemoteUser());
        User photographer = this.userService.findById(id);
        model.addAttribute("photographer", photographer);
        model.addAttribute("user", user);
        model.addAttribute("bodyContent", "bookaphotoshoot");
        return "master";
    }

    @PostMapping("/book/{id}")
    public String book(@PathVariable Long id,
                       @RequestParam String date,
                       HttpServletRequest request,
                       @RequestParam String time,
                       @RequestParam String partOfDay
                       ){
        String username = request.getRemoteUser();
        User loggedInUser = this.userService.findByUsername(username);
        User photographer = this.userService.findById(id);
        Photoshoot photoshoot = new Photoshoot(photographer, loggedInUser, date, time, partOfDay);
        this.photoshootService.save(photoshoot);
        return "redirect:/users/" + id;
    }

    @GetMapping("/booked/{id}")
    @Transactional
    public String getBookedForPhotographer(@PathVariable Long id,
                                           Model model) throws JsonProcessingException {
        User photographer = this.userService.findById(id);
        List<Photoshoot> photoshoots = this.photoshootService.findByPhotographer(photographer);
        model.addAttribute("list", photoshoots);
        model.addAttribute("bodyContent","bookedphotoshoots");
        return "master";
    }
}
