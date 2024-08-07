package com.example.twinklechat.common.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RoomUtil {

    public  String createRoomName(){

        return UUID.randomUUID().toString();
    }
}
