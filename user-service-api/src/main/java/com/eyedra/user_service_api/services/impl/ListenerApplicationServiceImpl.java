package com.eyedra.user_service_api.services.impl;

import com.eyedra.user_service_api.dto.request.ListenerApplicationReqDto;
import com.eyedra.user_service_api.dto.request.ListenerApplicationReviewReqDto;
import com.eyedra.user_service_api.dto.response.ListenerApplicationResponseDto;
import com.eyedra.user_service_api.entity.ListenerApplication;
import com.eyedra.user_service_api.entity.User;
import com.eyedra.user_service_api.exception.UserNotFoundException;
import com.eyedra.user_service_api.repository.ListenerApplicationRepository;
import com.eyedra.user_service_api.repository.UserRepository;
import com.eyedra.user_service_api.services.CloudinaryService;
import com.eyedra.user_service_api.services.ListenerApplicationService;
import com.eyedra.user_service_api.util.ListenerApplicationMapper;
import com.eyedra.user_service_api.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListenerApplicationServiceImpl implements ListenerApplicationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListenerApplicationRepository applicationRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ListenerApplicationMapper mapper;

    @Value("${application.upload.dir}")
    private String uploadDir;

    @Override
    public ListenerApplicationResponseDto submitApplication(ListenerApplicationReqDto request) throws IOException {
        User user = userRepository.findByUsername(request.getFullName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if user already has an application
        if (applicationRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("User already has an application");
        }
        // Check if user is already a listener
        if (Role.ROLE_LISTENER.equals(user.getRole())) {
            throw new RuntimeException("User is already a listener");
        }
        String imageUrl = cloudinaryService.uploadImage(request.getCertificationImage());


        // Create and save the application
        ListenerApplication application = ListenerApplication.builder()
                .user(user)
                .description(request.getDescription())
                .certificationImageUrl(imageUrl)
                .status(ListenerApplication.ApplicationStatus.PENDING)
                .build();

        ListenerApplication savedApplication = applicationRepository.save(application);

        return mapper.mapToListenerApplicationResponse(savedApplication);
    }

    @Override
    public List<ListenerApplicationResponseDto> getPendingApplications() {
        List<ListenerApplication> pendingApplications =
                applicationRepository.findByStatus(ListenerApplication.ApplicationStatus.PENDING);

        return pendingApplications.stream()
                .map(mapper::mapToListenerApplicationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ListenerApplicationResponseDto reviewApplication(ListenerApplicationReviewReqDto request) {
        ListenerApplication application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Check if application is still pending
        if (application.getStatus() != ListenerApplication.ApplicationStatus.PENDING) {
            throw new RuntimeException("Application is not in PENDING state");
        }
        // Update application
        application.setStatus(ListenerApplication.ApplicationStatus.valueOf(request.getStatus()));
        application.setReviewComment(request.getReviewComment());

        // If approved, upgrade user to listener
        if ("APPROVED".equals(request.getStatus())) {
            User user = application.getUser();
            user.setRole(Role.ROLE_LISTENER);
            userRepository.save(user);
        }
        ListenerApplication savedApplication = applicationRepository.save(application);

        return mapper.mapToListenerApplicationResponse(savedApplication);
    }

    @Override
    public ListenerApplicationResponseDto getApplicationStatus(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        ListenerApplication application = applicationRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No application found for this user"));

        return mapper.mapToListenerApplicationResponse(application);
    }

    @Override
    public String saveImage(MultipartFile file) throws IOException {
        // Create directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // Generate unique filename
        String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, filename);

        // Save file
        Files.write(filePath, file.getBytes());

        return "/api/listeners/files/" + filename;
    }
}
