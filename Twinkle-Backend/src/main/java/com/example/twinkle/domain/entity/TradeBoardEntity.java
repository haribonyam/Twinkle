package com.example.twinkle.domain.entity;


import com.example.twinkle.domain.entity.status.Condition;
import com.example.twinkle.dto.request.TradeBoardRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="tradeboard")
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

    @OneToMany(mappedBy = "tradeBoard", cascade = CascadeType.REMOVE)
    private List<FileEntity> files = new ArrayList<FileEntity>();

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime createdDate;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime updatedDate;

    @Enumerated(EnumType.STRING)
    @Column(name="item_condition")
    private Condition condition;

    private Long buyer;

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

    public void addFiles(FileEntity file){
        this.files.add(file);
        file.addTradeBoard(this);
    }
    @PrePersist
    public void CreatedDate(){
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public void updatedDate(){

        this.updatedDate = LocalDateTime.now();
    }

    public void updatePost(String title, String content, Integer price){
        this.title = title;
        this.content = content;
        this.price = price;
    }

    public void viewCountUp(){

        this.view++;
    }

    public void updateCondition(Condition condition){
        this.condition = condition;
    }
    public void setBuyer(Long buyer){
        this.buyer= buyer;
    }

}
