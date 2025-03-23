package com.eyedra.user_service_api.services;

import com.eyedra.user_service_api.dto.request.ListenerApplicationReqDto;
import com.eyedra.user_service_api.dto.request.ListenerApplicationReviewReqDto;
import com.eyedra.user_service_api.dto.response.ListenerApplicationResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ListenerApplicationService {

    ListenerApplicationResponseDto submitApplication(ListenerApplicationReqDto request) throws IOException;

    List<ListenerApplicationResponseDto> getPendingApplications();

    ListenerApplicationResponseDto reviewApplication(ListenerApplicationReviewReqDto request);

    ListenerApplicationResponseDto getApplicationStatus(String username);

    String saveImage(MultipartFile file) throws IOException;
}
