package com.eyedra.community_service_api.repository;

import com.eyedra.community_service_api.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageSpaceRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByMessageId(Long messageId);

    List<ChatMessage> findByCommunityId(Long communityId);

    List<ChatMessage> findByCommunityIdAndSenderId(Long communityId, Long senderId);

    List<ChatMessage> findByCommunityIdAfterAndTimestampAfterOrderByTimestampAsc(Long communityId, LocalDateTime timestamp);

    @Query("{'communityId': ?0, 'readByUserIds': { $ne: ?1 }}")
    List<ChatMessage> findUnreadMessagesByCommunityAndUser(Long communityId, Long userId);

    @Query(value = "{ 'communityId': ?0, 'readByUserIds': { $ne: ?1 } }", count = true)
    long countUnreadMessagesByCommunityAndUser(Long communityId, Long userId);

    // Added method to count all unread messages for a user across all communities
    @Query(value = "{ 'readByUserIds': { $ne: ?0 } }", count = true)
    long countAllUnreadMessagesByUser(Long userId);

    // Added method to find latest message from each community for a user
    @Query(value = "{ 'communityId': { $in: ?1 }, $or: [{ 'senderId': ?0 }, { 'readByUserIds': { $ne: ?0 } }] }",
            sort = "{ 'timestamp': -1 }")
    List<ChatMessage> findLatestMessagesByUserAndCommunities(Long userId, List<Long> communityIds);
}