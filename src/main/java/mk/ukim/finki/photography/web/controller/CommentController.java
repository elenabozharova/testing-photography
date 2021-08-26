package mk.ukim.finki.photography.web.controller;

import mk.ukim.finki.photography.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}")
    public String comment(@PathVariable Long id,
                          @RequestParam String comment,
                          @RequestParam Long userId,
                          Model model){
    if(comment=="" || comment.isEmpty()){
      model.addAttribute("error","You can not leave an empty comment");
      return "redirect:/photos";
     }
    this.commentService.addComment(comment, userId, id);
    model.addAttribute("bodyContent","listAll");
    return "redirect:/photos";
    }

}
