package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Controller
//@RequestMapping("songs")
public class DemoController {

    private final DemoService demoService;

    @Autowired
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @Controller
    public static class AuthInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(@SuppressWarnings("null") HttpServletRequest request, @SuppressWarnings("null") HttpServletResponse response, Object handler) throws Exception {
            HttpSession session = request.getSession();
            String username = (String) session.getAttribute("username");
            if (username == null) {
                response.sendRedirect("/?error=login");
                return false;
            }
            return true;
        }
    }

    @GetMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, HttpSession session) {
        // Save the username in session
        session.setAttribute("username", username);
        
        return "redirect:/songs";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Remove the username from session
        session.removeAttribute("username");
        // Redirect user to /
        return "redirect:/";
    }

    @GetMapping("/songs")
    public String getAll(Model model) {
        List<Song> songs = demoService.getAllSongs();
        model.addAttribute("songs", songs);
        return "listsongs";
    }

    @PostMapping("/songs")
    public String addSong(@ModelAttribute("newSong") Song newSong) {
        demoService.addSong(newSong);
        return "redirect:/songs";
    }

    @GetMapping("/songs/{id}")
    public String getSongDetails(@PathVariable("id") Long id, Model model) {
        Song song = demoService.getSongById(id);
        model.addAttribute("song", song);
        return "songdetails";
    }

    @DeleteMapping("/songs/delete/{id}")
    public String deleteSong(@PathVariable("id") Long id) {
        demoService.deleteSongById(id);
        return "redirect:/songs";
    }
}
