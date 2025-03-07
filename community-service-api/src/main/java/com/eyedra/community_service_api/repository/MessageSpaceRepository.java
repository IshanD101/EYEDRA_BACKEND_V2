package com.eyedra.community_service_api.repository;

import com.eyedra.community_service_api.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface MessageSpaceRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByMessageId(Long messageId);

    List<ChatMessage> findByCommunityIdAndSenderId(Long communityId, Long senderId);

    List<ChatMessage> findByCommunityIdAfterAndTimestampAfterOrderByTimestampAsc(Long communityId, LocalDateTime timestamp);

    @Query("{'groupId': ?0, 'readByUserIds': { $ne: ?1 }}")
    List<ChatMessage> findUnreadMessage(Long communityId, Long senderId);

    @Query(value = "{ 'groupId': ?0, 'readByUserIds': { $ne: ?1 } }", count = true)
    long countUnreadMessages(Long communityId, Long senderId);






}
