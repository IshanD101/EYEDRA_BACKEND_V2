package com.eyedra.community_service_api.repository;

import com.eyedra.community_service_api.entity.CommunitySpace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CommunitySpaceRepository extends MongoRepository<CommunitySpace, String> {

    Set<CommunitySpace> findByCommunityId(Long communityId);

    Set<CommunitySpace> findByMembersId(Long membersId);

    List<CommunitySpace> findByCreatorId(String creatorId);

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<CommunitySpace> findByTitle(String title);

    @Query("{ '_id': ?0, 'memberIds': ?1 }")
    Optional<CommunitySpace> findByIdAndMemberId(Long communityId, Long membersId);

}
