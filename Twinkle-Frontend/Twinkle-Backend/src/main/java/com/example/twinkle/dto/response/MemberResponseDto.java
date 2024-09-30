package com.example.twinkle.dto.response;


import com.example.twinkle.domain.entity.MemberEntity;
import lombok.Data;

@Data
public class MemberResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String email;

   public MemberResponseDto(MemberEntity member){
       this.id = member.getId();
       this.username = member.getUsername();
       this.nickname = member.getNickname();
       this.email = member.getEmail();
   }

}
