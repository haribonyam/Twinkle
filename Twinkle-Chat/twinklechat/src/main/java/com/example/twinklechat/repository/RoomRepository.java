package com.example.twinklechat.repository;

import com.example.twinklechat.domain.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity,Long> {

    @Query("SELECT r FROM RoomEntity r WHERE r.createMemberId = :memberId OR r.joinMemberId = :memberId ORDER BY r.createdDate DESC")
    List<RoomEntity> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END "+
           "FROM RoomEntity r"+" WHERE r.tradeBoardId = :tradeBoardId "+
            "AND (r.createMemberId =:memberId OR r.joinMemberId=:memberId)"
    )
    boolean existsByTradeBoardId(@Param("tradeBoardId")Long tradeBoardId, @Param("memberId")Long memberId);

    @Query("SELECT r FROM RoomEntity r WHERE r.tradeBoardId = :tradeBoardId AND (r.createMemberId = :memberId OR r.joinMemberId = :memberId)")
    Optional<RoomEntity> findByTradeBoardIdAndMemberId(@Param("tradeBoardId") Long tradeBoardId, @Param("memberId") Long memberId);

    List<RoomEntity> findAllByTradeBoardId(Long tradeBoardId);

    List<RoomEntity> findByTradeBoardId(Long tradeBoardId);
}
