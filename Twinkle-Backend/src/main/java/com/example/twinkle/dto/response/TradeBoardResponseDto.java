package com.example.twinkle.dto.response;


import com.example.twinkle.domain.entity.TradeBoardEntity;
import com.example.twinkle.domain.entity.status.Condition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeBoardResponseDto {

        private Long id;
        private Long memberId;
        private String nickname;
        private String title;
        private String content;
        private String condition;
        private String createdDate;
        private String updatedDate;
        private Integer price;

        public TradeBoardResponseDto(Long id, Long memberId, String nickname, String title, String content, Condition condition, LocalDateTime createdDate,LocalDateTime updatedDate,Integer price){
            this.id = id;
            this.memberId= memberId;
            this.nickname=nickname;
            this.title=title;
            this.content=content;
            this.condition=condition.name();
            this.createdDate=createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.updatedDate=updatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.price=price;

        }

        public static TradeBoardResponseDto toDto(TradeBoardEntity tradeBoardEntity){

            return new TradeBoardResponseDto(tradeBoardEntity.getId(),tradeBoardEntity.getMember().getId(),
                    tradeBoardEntity.getNickname(), tradeBoardEntity.getTitle(),tradeBoardEntity.getContent(),
                    tradeBoardEntity.getCondition(),tradeBoardEntity.getCreatedDate(),tradeBoardEntity.getUpdatedDate(),tradeBoardEntity.getPrice());

        }


}


