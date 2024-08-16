package com.example.twinklepay.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="pay")
public class PayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payId;

    private Long memberId;

    private Integer payMoney;

    @Builder
    public PayEntity(Long memberId,Integer payMoney){
        this.memberId = memberId;
        this.payMoney = payMoney;
    }

    public void decreasePayMoney(Integer payMoney){
        this.payMoney-=payMoney;
    }
    public void increasePayMoney(Integer payMoney){
        this.payMoney+=payMoney;
    }


}
