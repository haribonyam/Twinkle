package com.example.twinklechat.repository;

import com.example.twinklechat.domain.mongo.Chatting;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChattingRepository extends MongoRepository<Chatting, String> {

    List<Chatting> findAllByRoomId(Long roomId);
}
