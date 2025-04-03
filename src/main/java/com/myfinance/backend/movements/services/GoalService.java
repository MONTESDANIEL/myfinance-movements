package com.myfinance.backend.movements.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.myfinance.backend.movements.entities.AppGoal;

@Service
public class GoalService {

    private final WebClient webClient;

    public GoalService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://192.168.1.9:8083/api/goals/findById").build();
    }

    public AppGoal getGoalById(Long goalId) {
        if (goalId == null) {
            return null;
        }
        return this.webClient.get()
                .uri("/{id}", goalId)
                .retrieve()
                .bodyToMono(AppGoal.class)
                .block(); // Usa block() solo en aplicaciones no reactivas
    }
}
