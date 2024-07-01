package com.example.twinkle.domain.entity;


import com.example.twinkle.domain.entity.status.Condition;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name="tradeboard")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeBoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    private String nickname;

    private String title;

    private String content;

    private String category;

    private Integer view;

    private Integer price;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime createdDate;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime updatedDate;

    @Enumerated(EnumType.STRING)
    @Column(name="item_condition")
    private Condition condition;

    @Builder
    public TradeBoardEntity(String nickname,String content,String title,
                            Integer price,String category,MemberEntity member){
        this.member = member;
        this.nickname = nickname;
        this.content = content;
        this.title = title;
        this.price = price;
        this.category = category;
        this.condition = Condition.판매중;
        this.view = 0;
    }

    @PrePersist
    public void CreatedDate(){
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }
    @PreUpdate
    public void updatedDate(){
        this.updatedDate = LocalDateTime.now();
    }
    public void viewCountUp(){
        this.view++;
    }
}
