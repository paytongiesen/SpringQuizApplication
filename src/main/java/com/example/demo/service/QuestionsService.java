package com.example.demo.service;

import com.example.demo.model.Question;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionsService {

    private Map<Integer, Question> questions = new HashMap<>();

    public List<Question> loadQuizzes() {
        return new ArrayList<>(questions.values());
    }

    public void addQuiz(Question question) {
        questions.put(question.getId(), question);
    }

    public void editQuiz(Question question) {
        questions.put(question.getId(), question);
    }

    public void deleteQuiz(int id) {
        questions.remove(id);
    }

    public Question getQuiz(int id) {
        return questions.get(id);
    }
}