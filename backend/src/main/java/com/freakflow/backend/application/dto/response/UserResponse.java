package com.freakflow.backend.application.dto.response;

import java.time.Instant;
import java.util.List;

public class UserResponse {
    public Long id;
    public String name;
    public String avatarUrl;
    public String email;
    public int reputation;

    // Статистика
    public int questionsCount;
    public int answersCount;
    public int commentsCount;


    public String headline;
    public String bio;
    public String githubUrl;
    public Instant lastNameChangeAt;

    // Списки – для “недавних” вопросов/ответов
    public List<QuestionInProfileResponse> recentQuestions;
    //public List<AnswerSummaryDto> recentAnswers;

    // Бейджи
    //public List<BadgeResponse> badges;

    // Даты регистрации и последнего обновления профиля
    public Instant createdAt;
    public Instant updatedAt;
}
