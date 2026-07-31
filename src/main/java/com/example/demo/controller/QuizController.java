package com.example.demo.controller;

import com.example.demo.model.Question;
import com.example.demo.model.User;
import com.example.demo.service.QuestionsService;
import com.example.demo.service.QuizUserDetailsService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Map;

@Controller
public class QuizController {

    private final QuestionsService questionsService;
    private final QuizUserDetailsService userService;

    public QuizController(QuestionsService questionsService, QuizUserDetailsService userService) {
        this.questionsService = questionsService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute User user) {

        userService.registerUser(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());

        return "redirect:/login";
    }

    @GetMapping("/quizList")
    public String quizList(Model model) {
        model.addAttribute("questions", questionsService.loadQuizzes());
        return "quizList";
    }

    @GetMapping("/quiz")
    public String quiz(Model model) {
        model.addAttribute("questions", questionsService.loadQuizzes());
        return "quiz";
    }

    @GetMapping("/addQuiz")
    public String addQuiz(Model model) {
        model.addAttribute("question", new Question());
        return "addQuiz";
    }

    @PostMapping("/addQuiz")
    public String addQuiz(@ModelAttribute Question question) {
        questionsService.addQuiz(question);
        return "redirect:/quizList";
    }

    @GetMapping("/editQuiz/{id}")
    public String editQuiz(@PathVariable int id, Model model) {
        Question question = questionsService.getQuiz(id);
        model.addAttribute("question", question);
        return "editQuiz";
    }

    @PutMapping("/editQuiz")
    public String editQuiz(@ModelAttribute Question question) {
        questionsService.editQuiz(question);
        return "redirect:/quizList";
    }

    @DeleteMapping("/deleteQuiz/{id}")
    public String deleteQuiz(@PathVariable int id) {
        questionsService.deleteQuiz(id);
        return "redirect:/quizList";
    }

    @PostMapping("/submit")
    public String submit(
            @RequestParam Map<String, String> answers,
            Model model, RedirectAttributes redirectAttributes) {

        int score = 0;

        for (Question question : questionsService.loadQuizzes()) {

            String submittedAnswer =
                    answers.get(String.valueOf(question.getId()));

            if (submittedAnswer != null &&
                submittedAnswer.equals(question.getCorrectAnswer())) {

                score++;
            }
        }

        model.addAttribute("score", score);
        model.addAttribute(
                "total",
                questionsService.loadQuizzes().size());

        redirectAttributes.addFlashAttribute("score", score);
        redirectAttributes.addFlashAttribute("total", questionsService.loadQuizzes().size());

        return "redirect:/result";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}