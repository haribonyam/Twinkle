package com.example.twinkle.controller.user;

import com.example.twinkle.dto.request.MailDto;
import com.example.twinkle.service.mail.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/mailConfirm")
    public ResponseEntity<Map<String,String>> confirmMail(@RequestBody MailDto mailReq) throws MessagingException {
        String code = mailService.sendSimpleMessage(mailReq.getEmail());
       Map<String,String> response = new HashMap<>();
       response.put("code",code);
        return ResponseEntity.ok(response);
    }
}
