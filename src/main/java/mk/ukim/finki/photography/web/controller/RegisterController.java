package mk.ukim.finki.photography.web.controller;

import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.photography.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.photography.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam (required=false) String error){
    return "register";
    }

    @PostMapping
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam(required = false) String linkedin,
                           @RequestParam(required = false) String instagram,
                           @RequestParam(required = false) String facebook,
                           @RequestParam Role role) {
        try{
            this.userService.register(username, password, repeatedPassword, name, surname, role, linkedin, instagram, facebook);
            return "redirect:/login";
        } catch (InvalidArgumentsException | PasswordsDoNotMatchException exception) {
            return "redirect:/register?error=" + exception.getMessage();
        }
    }


}
