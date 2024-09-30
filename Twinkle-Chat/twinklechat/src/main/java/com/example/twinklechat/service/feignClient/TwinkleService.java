package com.example.twinklechat.service.feignClient;


import com.example.twinklechat.dto.response.MemberResponseDto;
import com.example.twinklechat.dto.response.TradeBoardResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "twinkle", url="http://localhost:8080")
@Qualifier
public interface TwinkleService {

    @GetMapping("/user/{nickname}")
    ResponseEntity<MemberResponseDto> findByNickname(@PathVariable(value="nickname") String nickname);

    @GetMapping("/tradeboard/{id}")
    ResponseEntity<TradeBoardResponseDto> TradeBoardPost(@PathVariable(value="id") Long id);

    @GetMapping("/service/{username}")
    ResponseEntity<MemberResponseDto> findByUsername(@PathVariable(value="username")String username);

}
