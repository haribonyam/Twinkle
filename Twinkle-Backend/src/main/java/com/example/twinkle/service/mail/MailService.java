package com.example.twinkle.service.mail;

import jakarta.mail.IllegalWriteException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;

    public String createRandKey(){
        StringBuilder key = new StringBuilder();
        Random rand = new Random();

        for(int i=0;i<6;i++){
            int idx = rand.nextInt(3);
            switch(idx){
                case 0:
                    key.append((char)((int)rand.nextInt(26)+97));
                    break;
                case 1:
                    key.append((char)((int)rand.nextInt(26)+65));
                    break;
                case 2:
                    key.append(rand.nextInt(10));
                    break;
            }
        }
        return key.toString();
    }

    public MimeMessage createMimeMessage(String to,String key) throws MessagingException{
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject("반짝 회원 이메일 인증");
        message.addRecipients(MimeMessage.RecipientType.TO,to);

        String body = "";
        body+="<div style=\"margin:100px;\"></div>";
        body+="<h2 style=\"text-align: center;\">반짝 회원 인증 메일</h2>";
        body+="<br><br><div style=\"text-align: center;\">";
        body+="<div>CODE : <strong>"+key+"</strong>";
        body+="</div><br><br><h5>홈페이지로 돌아가 해당 코드를 입력해 주세요.</h5></div>";
        message.setText(body,"UTF-8","html");
        return message;
    }

    public String sendSimpleMessage(String to) throws MessagingException {
        String key = createRandKey();
        MimeMessage message = createMimeMessage(to,key);
        try{
            javaMailSender.send(message);
        }catch(MailException e){
            log.info("{}",e);
            throw new IllegalWriteException("메일 발송 중 오류가 발생했습니다.");
        }
        return key;
    }
}
