package com.eyedra.user_service_api.services;

import com.eyedra.user_service_api.dto.request.ListenerProfileDto;
import com.eyedra.user_service_api.dto.request.ListenerProfileUpdateReqDto;
import com.eyedra.user_service_api.dto.request.ListenerStatsDto;

public interface ListenerService {
    ListenerProfileDto getListenerProfile(String username);
    void updateListenerProfile(String username, ListenerProfileUpdateReqDto updateDto);
    ListenerStatsDto getListenerStats(String username);
}
