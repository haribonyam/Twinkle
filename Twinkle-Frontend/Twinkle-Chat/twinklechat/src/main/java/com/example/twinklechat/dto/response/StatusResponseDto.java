package com.example.twinklechat.dto.response;

import com.example.twinklechat.domain.entity.RoomEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusResponseDto {

    private Integer status;
    private Object data;

    public StatusResponseDto(Integer status){
        this.status = status;
    }
    public static StatusResponseDto addStatus(Integer status){
        return new StatusResponseDto(status);
    }
    public static StatusResponseDto addStatus(Integer status, RoomEntity data){
        return new StatusResponseDto(status,data);
    }
    public static StatusResponseDto success(){
        return new StatusResponseDto(200);
    }
}
