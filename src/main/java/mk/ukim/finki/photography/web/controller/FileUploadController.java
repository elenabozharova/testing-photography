package mk.ukim.finki.photography.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("newimage")
public class FileUploadController {
    public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

    @RequestMapping("/")
    public String showUploadPage(Model model){
        return "upload";
    }

    @PostMapping("/upload")
    public String upload(Model model, @RequestParam("files") MultipartFile[]files) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        for(MultipartFile file: files){
            Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
        }
        return "uploaded";
    }

}
