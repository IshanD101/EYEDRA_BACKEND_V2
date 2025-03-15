package com.eyedra.user_service_api.services.impl;

import com.eyedra.user_service_api.dto.request.ListenerProfileDto;
import com.eyedra.user_service_api.dto.request.ListenerProfileUpdateReqDto;
import com.eyedra.user_service_api.dto.request.ListenerStatsDto;
import com.eyedra.user_service_api.services.ListenerService;

public class ListenerServiceImpl implements ListenerService {
    @Override
    public ListenerProfileDto getListenerProfile(String username) {
        return null;
    }

    @Override
    public void updateListenerProfile(String username, ListenerProfileUpdateReqDto updateDto) {

    }

    @Override
    public ListenerStatsDto getListenerStats(String username) {
        return null;
    }
}
