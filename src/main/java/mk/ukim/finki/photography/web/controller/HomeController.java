package mk.ukim.finki.photography.web.controller;

import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/home", ""})
public class HomeController {
    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showHome(Model model){
        model.addAttribute("bodyContent", "home");
        return "master";
    }
}
