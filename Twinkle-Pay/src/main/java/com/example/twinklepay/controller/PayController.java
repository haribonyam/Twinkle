package com.example.twinklepay.controller;

import com.example.twinklepay.dto.request.PayRequestDto;
import com.example.twinklepay.dto.response.PayResponseDto;
import com.example.twinklepay.service.PayService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PayController {

    @Value("${import.key}")
    private String apiKey;

    @Value("${import.secret}")
    private String secretKey;

    private IamportClient iamportClient;
    private final PayService payService;
    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    @PostMapping("/payment/validation/{imp_uid}")
    public IamportResponse<Payment> validateIamport(@PathVariable String imp_uid) throws IOException {
        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
        log.info("결제 요청 응답. 결제 내역 - 주문 번호: {}", payment.getResponse().getMerchantUid());
        return payment;
    }
    @GetMapping("/payment/{memberId}")
    public ResponseEntity<PayResponseDto> getPayMoney(@PathVariable Long memberId){
        return ResponseEntity.ok(payService.getPayMoney(memberId));
    }
    @PostMapping("/payment/charge")
    public ResponseEntity<PayResponseDto> chargePayMoney(@RequestBody PayRequestDto payRequestDto){

        return ResponseEntity.ok(payService.chargePayMoney(payRequestDto));
    }

    @PostMapping("/payment/trade")
    public ResponseEntity<PayResponseDto> tradePayMoney(@RequestBody PayRequestDto payRequestDto){

        return ResponseEntity.ok(payService.tradePayMoney(payRequestDto));
    }



}
