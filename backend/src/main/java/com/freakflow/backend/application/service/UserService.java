package com.freakflow.backend.application.service;

import com.freakflow.backend.application.dto.request.UserUpdateRequest;
import com.freakflow.backend.application.dto.response.QuestionInProfileResponse;
import com.freakflow.backend.application.dto.response.UserResponse;
import com.freakflow.backend.domain.model.Question;
import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AvatarService avatarService;

    public UserResponse getUserById(Long id) {
        User user= userRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        UserResponse userResponse = new UserResponse();
        userResponse.id = user.getId();
        userResponse.name = user.getName();
        userResponse.email=user.getEmail().getValue();
        userResponse.questionsCount=user.getQuestions().size();
        userResponse.answersCount=user.getAnswers().size();
        userResponse.createdAt=user.getCreatedAt();
        userResponse.recentQuestions=user.getQuestions().stream() .sorted(Comparator.comparing(Question::getCreatedAt).reversed())
                .limit(5)
                .map(question -> {
                    QuestionInProfileResponse questionInProfileResponse = new QuestionInProfileResponse();
                    questionInProfileResponse.id = question.getId();
                    questionInProfileResponse.title = question.getTitle();
                    questionInProfileResponse.votesCount=question.getVotes().size();
                    questionInProfileResponse.answersCount=question.getAnswers().size();
                    questionInProfileResponse.createdAt=question.getCreatedAt();
                    return questionInProfileResponse;
                }).toList();
        userResponse.avatarUrl=user.getAvatarUrl();
        userResponse.bio=user.getBio();
        userResponse.headline=user.getHeadline();
        userResponse.githubUrl=user.getGithubUrl();
        userResponse.lastNameChangeAt=user.getLastNameChangeAt();
        return userResponse;
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        userRepository.delete(user);
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        if (userUpdateRequest.name != null) {
            if (userUpdateRequest.name.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be blank");
            }
            Instant lastChange = user.getLastNameChangeAt();
            if (lastChange != null && Instant.now().isBefore(lastChange.plus(14, DAYS))) {
                long daysLeft = DAYS.between(Instant.now(), lastChange.plus(14, DAYS));
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "You can change name once in 14 days. Days left: " + daysLeft);
            }
            if (userRepository.findByName(userUpdateRequest.name).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User name already exists");
            }
            user.setName(userUpdateRequest.name);
            user.setLastNameChangeAt(Instant.now());
        }


        if (userUpdateRequest.headline != null) {
            user.setHeadline(userUpdateRequest.headline);
        }
        if (userUpdateRequest.bio != null) {
            user.setBio(userUpdateRequest.bio);
        }
        if (userUpdateRequest.githubUrl != null) {
            user.setGithubUrl(userUpdateRequest.githubUrl);
        }

        UserResponse userResponse = new UserResponse();
        userResponse.id = user.getId();
        userResponse.email=user.getEmail().getValue();
        userResponse.questionsCount=user.getQuestions().size();
        userResponse.answersCount=user.getAnswers().size();
        userResponse.createdAt=user.getCreatedAt();
        userResponse.recentQuestions=user.getQuestions().stream() .sorted(Comparator.comparing(Question::getCreatedAt).reversed())
                .limit(5)
                .map(question -> {
                    QuestionInProfileResponse questionInProfileResponse = new QuestionInProfileResponse();
                    questionInProfileResponse.id = question.getId();
                    questionInProfileResponse.title = question.getTitle();
                    questionInProfileResponse.votesCount=question.getVotes().size();
                    questionInProfileResponse.answersCount=question.getAnswers().size();
                    questionInProfileResponse.createdAt=question.getCreatedAt();
                    return questionInProfileResponse;
                }).toList();
        userResponse.name = user.getName();
        userResponse.headline=user.getHeadline();
        userResponse.lastNameChangeAt=user.getLastNameChangeAt();
        userResponse.githubUrl=user.getGithubUrl();
        userResponse.avatarUrl=user.getAvatarUrl();
        userResponse.bio=user.getBio();

        return userResponse;
    }

    @Transactional
    public void updateAvatar(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        try {
            String url = avatarService.store(file, userId);
            user.setAvatarUrl(url);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store avatar", e);
        }
    }
}
